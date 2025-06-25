package com.s3reportlambda.S3method.impl;


// import java.io.File;
// import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// import java.nio.file.Paths;
// import java.nio.file.Path;
import com.s3reportlambda.S3method.IuseCaseS3Methods;
import com.s3reportlambda.domain.S3FileInfo;


import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

public class ImplUseCaseS3Method implements IuseCaseS3Methods {
    private final S3Client s3Client;

    public ImplUseCaseS3Method() {
        this.s3Client = S3Client.create();

    }

    @Override
    public String uploaFile(String userPoolId, String tennatName, InputStream file, String nameFile)
            throws IOException {
        try {
            // ByteArrayInputStream fileForm = new ByteArrayInputStream(file.readAllBytes());
            // String nombrePdf = nameFile.replaceAll(".xls", ".pdf");
            String keyS3 = tennatName + "/" + userPoolId + "/reports/" + nameFile;
            // Crear solicitud para subir el objeto
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket("my-spring-bucket-eligomez")
                    .key(keyS3)
                    .contentType("application/pdf")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file, file.available()));

            return "Se ha subido el archivo de forma correcta";
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF desde Excel", e);
        }
    }

    @Override
    public byte[] downloadFile(String userPoolId, String tennatName, String key) throws IOException {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket("my-spring-bucket-eligomez")
                // Key es el nombre del Archivo , Claro que tambien es el path pero hacemos
                // referencia al parametro Key

                .key(tennatName + "/" + userPoolId + "/reports/" + key)
                .build();

        // debemos tener en cueta toda la ruta para ello
        ResponseBytes<GetObjectResponse> objecBytes = this.s3Client.getObjectAsBytes(getObjectRequest);
        return objecBytes.asByteArray();
    }

    @Override
    public Map<String, List<S3FileInfo>> listsFiles(String userPoolId, String tennatName) {
        try {

            String path = tennatName + "/" + userPoolId + "/";

            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket("my-spring-bucket-eligomez")
                    .prefix(path)
                    .build();

            ListObjectsV2Response response = this.s3Client.listObjectsV2(request);
            List<S3Object> objetos = response.contents();

            List<S3FileInfo> propertiesFile = new ArrayList<>();
            for (S3Object obj : objetos) {

                S3FileInfo fileInfo = new S3FileInfo();

                fileInfo.setKey(obj.key());
                fileInfo.setSize(obj.size());
                fileInfo.setLastModified(obj.lastModified().toString());

                propertiesFile.add(fileInfo);

            }
            Map<String, List<S3FileInfo>> resultMap = new HashMap<>();
            resultMap.put(userPoolId, propertiesFile);

            return resultMap;

        } catch (Exception e) {
            throw new RuntimeException("Error al listar archivos desde S3", e);
        }

    }

}
