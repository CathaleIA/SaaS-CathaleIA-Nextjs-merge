package com.s3reportlambda.LambdaHandlers;

import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.databind.ObjectMapper;                                                                                   
import com.s3reportlambda.DtoLambda.NewObjectRequest;
import com.s3reportlambda.DtoLambda.UploadResponse;
import com.s3reportlambda.S3method.IuseCaseS3Methods;
import com.s3reportlambda.S3method.impl.ImplUseCaseS3Method;

public class UploadLambda implements RequestStreamHandler {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private IuseCaseS3Methods iuseCaseS3Methods;

  public UploadLambda() {
    this.iuseCaseS3Methods = new ImplUseCaseS3Method();
  }

  @Override
  public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {

    try {
      // Leer el body del request como JSON
      APIGatewayV2HTTPEvent event = objectMapper.readValue(input, APIGatewayV2HTTPEvent.class);
      NewObjectRequest request = objectMapper.readValue(event.getBody(), NewObjectRequest.class);

      String userPoolId = request.getUserPoolId();
      String tenantName = request.getTenantName();
      String fileName = request.getFileName();


      byte[] filebase64 = Base64.getDecoder().decode(request.getFileBase64());
      InputStream archivoSubirBase64 = new ByteArrayInputStream(filebase64);
      this.iuseCaseS3Methods.uploaFile(userPoolId, tenantName, archivoSubirBase64, fileName);


      UploadResponse responseBody = new UploadResponse("Archivos subidos correctamente");


      Map<String, String> headers = new HashMap<>();
      headers.put("Content-Type", "application/json");
      headers.put("Access-Control-Allow-Origin", "*");

      APIGatewayV2HTTPResponse response = APIGatewayV2HTTPResponse.builder()
          .withStatusCode(200)
          .withBody(objectMapper.writeValueAsString(responseBody))
          .withHeaders(headers)
          .build();

      //  Escribir salida
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
