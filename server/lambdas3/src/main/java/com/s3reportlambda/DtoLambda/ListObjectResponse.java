package com.s3reportlambda.DtoLambda;

import java.util.List;
import java.util.Map;

import com.s3reportlambda.domain.S3FileInfo;

public class ListObjectResponse {
    
    private Map<String , List<S3FileInfo>> MapObjetos;

    public Map<String, List<S3FileInfo>> getMapObjetos() {
        return MapObjetos;
    }

    public void setMapObjetos(Map<String, List<S3FileInfo>> mapObjetos) {
        MapObjetos = mapObjetos;
    }

    public ListObjectResponse(Map<String, List<S3FileInfo>> mapObjetos) {
        MapObjetos = mapObjetos;
    }

    public ListObjectResponse() {
    }
    


}
