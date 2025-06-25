package com.s3reportlambda.S3method;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.s3reportlambda.domain.S3FileInfo;

public interface IuseCaseS3Methods {
        // Caso de uso para Subir un Archivo al bucket
    // Boolean uploadFile(String bucketName, String key, Path fileLocation);
    String uploaFile(String userPoolId, String tennatName, InputStream file, String nameFile) throws IOException;
    
    // Caso de Uso para Descargar un Archivo de un Bucket
    byte[] downloadFile(String userPoolId,String tennatName, String key) throws IOException;

    Map<String ,List<S3FileInfo>> listsFiles(String userPoolId, String tennatName);
}
