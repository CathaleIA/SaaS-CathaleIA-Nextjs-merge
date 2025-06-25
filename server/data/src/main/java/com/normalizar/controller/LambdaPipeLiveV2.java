package com.normalizar.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.joda.time.Instant;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.normalizar.domain.GraficaData;
import com.normalizar.domain.GraficaDataExi;
import com.normalizar.domain.Report;
import com.normalizar.domain.ResponseReport;
import com.normalizar.repositoryDynamoDB.ImplUseCaseDynamoDB;
import com.normalizar.repositoryDynamoDB.IuseCaseDynamoDB;
import com.normalizar.repositoryDynamoDB.entity.MetaDataReport;
import com.normalizar.templateMemori.ImpleCaseMemory;
import com.normalizar.templateMemori.ItemplateCase;
import com.normalizar.thymeleaRender.ThymeleaRenderTeamplate;
import com.normalizar.utility.ImappingUseCase;
import com.normalizar.utility.impl.ImplMappingUseCase;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.http.HttpClient;

public class LambdaPipeLiveV2 implements RequestStreamHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private ItemplateCase itemplateCase;
    private IuseCaseDynamoDB iuseCaseDynamoDB;
    private final DynamoDbClient dbClient;
    private ImappingUseCase imappingUseCase;

    public LambdaPipeLiveV2() {
        this.itemplateCase = new ImpleCaseMemory();
        this.dbClient = DynamoDbClient.create();
        this.iuseCaseDynamoDB = new ImplUseCaseDynamoDB();
        this.imappingUseCase = new ImplMappingUseCase();
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        try {
            APIGatewayV2HTTPEvent event = objectMapper.readValue(input, APIGatewayV2HTTPEvent.class);
            Report report = objectMapper.readValue(event.getBody(), Report.class);

            // MApeamos los objetos de la peticion (Request)

            String activo = report.getActivo();
            String tenant = report.getTenant();
            String poolUserId = report.getPoolUserId();
            String archivoToFront = report.getArchivoToFront();

            // Payload para lambda "Normalizar"
            Map<String, String> payloadInterno = Map.of("file", archivoToFront);
            // <PajazoMental> y aca podemos scarlos a una base datos creo yo como un puerto
            // de salida (DB , *dinnamoDB*)
            String jsonInterno = objectMapper.writeValueAsString(payloadInterno);
            // Armamos la el requesr y la cargamos con el payload
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://e989ua8tf9.execute-api.us-east-1.amazonaws.com/dev/inter_normalizar"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInterno))
                    .build();

            HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Verifica el código de estado HTTP
            if (httpResponse.statusCode() != 200) {
                throw new RuntimeException("Error al invocar el servicio de normalización: " + httpResponse.body());
            }

            // Repuesta de la lambda de normalizacion captado El body
            String jsonResult = httpResponse.body();
            System.out.println("Este es el resultado real o crudo de la lambda normalizar" + jsonResult);
            // Logs
            System.out.println("Respuesta del servicio de normalización:");
            System.out.println(jsonResult);
            Map<String, Object> allChartData = objectMapper.readValue(jsonResult, Map.class);
            GraficaData graficaCNData = objectMapper.convertValue(allChartData.get("GraficaCN"), GraficaData.class);
            GraficaData graficaRCNData = objectMapper.convertValue(allChartData.get("GraficaRCN"), GraficaData.class);
            GraficaDataExi graficaDataExi = objectMapper.convertValue(allChartData.get("GraficaExitacion"),
                    GraficaDataExi.class);
            // Llamada s3

            String fileName = ""; // Nombre sugerido por el front
            if (fileName == null || fileName.isEmpty()) {
                fileName = "data_normalizada" + UUID.randomUUID().toString() + ".json"; // Generar nombre único si no se
                // proporciona
            } else {
                if (!fileName.toLowerCase().endsWith(".json")) { // Asegurarse de que tenga la extensión .pdf
                    fileName += ".json";
                }
            }

            String normallizedJsonString = objectMapper.writeValueAsString(jsonResult);
            String base64EncodedJson = Base64.getEncoder()
                    .encodeToString(normallizedJsonString.getBytes(StandardCharsets.UTF_8));

            // **CORRECCIÓN AQUÍ: Crear un payload que coincida con NewObjectRequest**
            Map<String, String> payloadS3 = new HashMap<>(); // Usar solo String para valores simples
            payloadS3.put("userPoolId", report.getPoolUserId());
            payloadS3.put("tenantName", report.getTenant());
            payloadS3.put("fileName", fileName); // Nombre del archivo directamente
            payloadS3.put("fileBase64", base64EncodedJson); // Contenido Base64 directamente

            String jsonInternoS3 = objectMapper.writeValueAsString(payloadS3);

            HttpRequest requestS3 = HttpRequest.newBuilder()
                    .uri(URI.create("https://e989ua8tf9.execute-api.us-east-1.amazonaws.com/dev/upLoadFile"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInternoS3))
                    .build();

            HttpResponse<String> httpResponseS3 = httpClient.send(requestS3, HttpResponse.BodyHandlers.ofString());

            // Verifica el código de estado HTTP
            if (httpResponseS3.statusCode() != 200) {
                throw new RuntimeException("Error al invocar el servicio de S3: " + httpResponseS3.body());
            }

            // DynamoDB
            String report_id = UUID.randomUUID().toString();
            String timestamp = Instant.now().toString();

            // Create metaDataReport

            MetaDataReport metaDataReportDTO = new MetaDataReport();
            String pathJsonS3 = tenant + "/" + poolUserId + "/reports/" + fileName;
            metaDataReportDTO.setActivo(activo);
            metaDataReportDTO.setTenant_id(tenant);
            metaDataReportDTO.setPoolUserId(poolUserId);

            // el item no se edita por que el report_id es diferente
            metaDataReportDTO.setReport_id(report_id);
            metaDataReportDTO.setS3JsonPath(pathJsonS3);
            metaDataReportDTO.setS3PdfPath("Missing But Soon");
            metaDataReportDTO.setTimestamp(timestamp);
            metaDataReportDTO.setEstado("NORMALIZADO");

            String saveItem = this.iuseCaseDynamoDB.CreateItem(metaDataReportDTO, dbClient, "tablaReports");
            System.out.println(saveItem);

            Map<String, Object> modelo = imappingUseCase.mapJsonToThymeleafModel(jsonResult, report);

            String template = itemplateCase.selectTemplate(activo);
            System.out.println(activo);
            // lo que debemos hacer creo yo es cambiar el proceso de este fragmento para
            // arriba
            String html = ThymeleaRenderTeamplate.render(template, modelo);
            System.out.println("Estos son los objetos que esta tomando para graficar : " + template +"\ny el modelo" +modelo);
            ResponseReport reponseBody = new ResponseReport(metaDataReportDTO.getReport_id(), html, graficaCNData,
                    graficaRCNData, graficaDataExi);

            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Access-Control-Allow-Origin", "*");

            APIGatewayV2HTTPResponse response = APIGatewayV2HTTPResponse.builder()
                    .withStatusCode(200)
                    .withBody(objectMapper.writeValueAsString(reponseBody))
                    .withHeaders(headers)
                    .build();

            // Escribir salida
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
