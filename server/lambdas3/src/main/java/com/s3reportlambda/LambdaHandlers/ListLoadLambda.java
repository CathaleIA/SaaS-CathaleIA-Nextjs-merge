package com.s3reportlambda.LambdaHandlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.s3reportlambda.DtoLambda.ListObjectResponse;
import com.s3reportlambda.DtoLambda.ListObjectsRequest;
import com.s3reportlambda.S3method.IuseCaseS3Methods;
import com.s3reportlambda.S3method.impl.ImplUseCaseS3Method;

public class ListLoadLambda implements RequestStreamHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private IuseCaseS3Methods iuseCaseS3Methods;

    public ListLoadLambda() {
        this.iuseCaseS3Methods = new ImplUseCaseS3Method();
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        try {
            // Leer el evento completo desde API Gateway
            APIGatewayV2HTTPEvent event = objectMapper.readValue(input, APIGatewayV2HTTPEvent.class);

            // Parsear solo el cuerpo del evento como DownloadRequest
            ListObjectsRequest request = objectMapper.readValue(event.getBody(), ListObjectsRequest.class);

            String userPoolId = request.getUserPoolid();
            String tenantName = request.getTenantName();

            ListObjectResponse responseDto = new ListObjectResponse(
                    this.iuseCaseS3Methods.listsFiles(userPoolId, tenantName));

            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Access-Control-Allow-Origin", "*");

            APIGatewayV2HTTPResponse response = APIGatewayV2HTTPResponse.builder()
                    .withStatusCode(200)
                    .withBody(objectMapper.writeValueAsString(responseDto))
                    .withHeaders(headers)
                    .build();

            // Escribir salida
            output.write(objectMapper.writeValueAsBytes(response));

        } catch (Exception e) {
            // Manejo de errores
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
