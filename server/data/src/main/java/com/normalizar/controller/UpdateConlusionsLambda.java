package com.normalizar.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.normalizar.domain.ResponseObjetoFinal;
import com.normalizar.repositoryDynamoDB.entity.UpdateConlucions;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

public class UpdateConlusionsLambda implements RequestStreamHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final DynamoDbClient dbClient;

    public UpdateConlusionsLambda() {
        this.dbClient = DynamoDbClient.create();
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        try {
            APIGatewayV2HTTPEvent event = objectMapper.readValue(input, APIGatewayV2HTTPEvent.class);
            UpdateConlucions updateConlucions = objectMapper.readValue(event.getBody(), UpdateConlucions.class);

            String report_id = updateConlucions.getReport_id();
            String tenant_id = updateConlucions.getTenant_id();
            List<String> conclusions = updateConlucions.getConcluciones();
            if (conclusions != null && !conclusions.isEmpty()) {
                List<AttributeValue> attributeValues = conclusions.stream()
                        .map(s -> AttributeValue.builder().s(s).build())
                        .toList();

                UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                        .tableName("tablaReports")
                        .key(Map.of(
                                "tenant_id", AttributeValue.builder().s(tenant_id).build(),
                                "report_id", AttributeValue.builder().s(report_id).build()))
                        .updateExpression("SET conclusiones = :val")
                        .expressionAttributeValues(Map.of(":val", AttributeValue.builder().l(attributeValues).build()))
                        .build();

                dbClient.updateItem(updateItemRequest);

                ResponseObjetoFinal reponseBody = new ResponseObjetoFinal("Se a Actualizado DB reportes");
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

            }

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
