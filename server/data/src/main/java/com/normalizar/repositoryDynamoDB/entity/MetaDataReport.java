package com.normalizar.repositoryDynamoDB.entity;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaDataReport {
    private String tenant_id;
    private String report_id;// Estandar con el que se elaboro el teamplate del reporte
    private String activo;
    private String poolUserId;
    private String s3JsonPath;
    private String s3PdfPath;
    private String timestamp;
    private String estado;


}
