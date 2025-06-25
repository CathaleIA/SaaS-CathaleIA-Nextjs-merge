package com.normalizar.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.normalizar.domain.PdfRenderRequest;

import com.normalizar.domain.ResponseObjetoFinal;
import com.normalizar.templateMemori.ImpleCaseMemory;
import com.normalizar.templateMemori.ItemplateCase;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RenderPdfLambda implements RequestStreamHandler {
        private static final Logger logger = LoggerFactory.getLogger(RenderPdfLambda.class);
        private static final ObjectMapper objectMapper = new ObjectMapper();
        private final HttpClient httpClient = HttpClient.newHttpClient();
        private ItemplateCase itemplateCase;
        private final DynamoDbClient dbClient;

        public RenderPdfLambda() {
                this.dbClient = DynamoDbClient.create();
                this.itemplateCase = new ImpleCaseMemory();
        }

        @Override
        public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
                try {
                        APIGatewayV2HTTPEvent event = objectMapper.readValue(input, APIGatewayV2HTTPEvent.class);
                        PdfRenderRequest pdfRenderRequest = objectMapper.readValue(event.getBody(),
                                        PdfRenderRequest.class);
                        String encodedArchivoHtml = pdfRenderRequest.getArchivoHtml();
                        String tenant_id = pdfRenderRequest.getTenant_id();
                        String poolUserId = pdfRenderRequest.getPoolUserId();
                        String report_id = pdfRenderRequest.getReport_id();
                        String fileName = pdfRenderRequest.getFileName();

                        // ========= CREACION DE ARCHIVO EN MEMORIA =============

                        String decodedArchivoHtml = new String(Base64.getDecoder().decode(encodedArchivoHtml),
                                        StandardCharsets.UTF_8);

                        // =============================== ENCODE HTML EN BASE64 PARA ENVIO DEL ARCHIVO
                        // EN BASE S3 ===============================

                        // Uso de implementacion para renderizacion del pdf
                        byte[] pdfBytes = this.itemplateCase.createPDFComponent(decodedArchivoHtml);
                        System.out.println(
                                        "===============================  PDF generado Log ===============================");
                        logger.info("HTML decodificado: {}", decodedArchivoHtml);
                        logger.info("PDF generado con tamaño: {} bytes", pdfBytes.length);

                        String base64EncodedPdf = Base64.getEncoder().encodeToString(pdfBytes);
                        logger.info("PDF codificado en Base64: {}", base64EncodedPdf.substring(0, 50) + "...");

                        // =========== LLAMAR LAMBDA S3 ====================
                        String fileNamePDF = fileName + ".pdf";
                        Map<String, String> payloadS3 = new HashMap<>(); // Usar solo String para valores simples
                        payloadS3.put("userPoolId", poolUserId);
                        payloadS3.put("tenantName", tenant_id);
                        payloadS3.put("fileName", fileNamePDF); // Nombre del archivo directamente
                        payloadS3.put("fileBase64", base64EncodedPdf); // Contenido Base64 directamente

                        String pdS3File = objectMapper.writeValueAsString(payloadS3);

                        HttpRequest requestS3 = HttpRequest.newBuilder()
                                        .uri(URI.create("https://e989ua8tf9.execute-api.us-east-1.amazonaws.com/dev/upLoadFile"))
                                        .header("Content-Type", "application/json")
                                        .POST(HttpRequest.BodyPublishers.ofString(pdS3File))
                                        .build();

                        HttpResponse<String> httpResponseS3 = httpClient.send(requestS3,
                                        HttpResponse.BodyHandlers.ofString());

                        // Verifica el código de estado HTTP
                        if (httpResponseS3.statusCode() != 200) {
                                throw new RuntimeException(
                                                "Error al invocar el servicio de S3: " + httpResponseS3.body());
                        }

                        // ========== ACTUALIZAR EL PATH EN DYNAMODB ========================

                        String pathS3 = tenant_id + "/" + poolUserId + "/report/" + fileNamePDF;
                        UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                                        .tableName("tablaReports")
                                        .key(Map.of(
                                                        "tenant_id", AttributeValue.builder().s(tenant_id).build(),
                                                        "report_id", AttributeValue.builder().s(report_id).build()))
                                        // Correctly update multiple attributes in a single update expression
                                        .updateExpression("SET s3_pdf_path = :pathVal, estado = :statusVal")
                                        .expressionAttributeValues(Map.of(
                                                        ":pathVal", AttributeValue.builder().s(pathS3).build(),
                                                        ":statusVal", AttributeValue.builder().s("COMPLETADO").build()))
                                        .build();

                        dbClient.updateItem(updateItemRequest);

                        ResponseObjetoFinal reponseBody = new ResponseObjetoFinal("Se creo el Archivo PDF");

                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Access-Control-Allow-Origin", "*");

                        APIGatewayV2HTTPResponse response = APIGatewayV2HTTPResponse.builder()
                                        .withStatusCode(200)
                                        .withBody(objectMapper.writeValueAsString(reponseBody))
                                        .withHeaders(headers)
                                        .build();
                        output.write(objectMapper.writeValueAsBytes(response));

                } catch (Exception e) {
                        Map<String, String> errorBody = Map.of("error", e.getMessage());

                        Map<String, String> errorHeaders = new HashMap<>();
                        errorHeaders.put("Content-Type", "application/json");
                        errorHeaders.put("Access-Control-Allow-Origin", "*");

                        APIGatewayV2HTTPResponse errorResponse = APIGatewayV2HTTPResponse.builder()
                                        .withStatusCode(500)
                                        .withBody(objectMapper.writeValueAsString(errorBody))
                                        .withHeaders(errorHeaders)
                                        .build();

                        output.write(objectMapper.writeValueAsBytes(errorResponse));
                }
        }

}
