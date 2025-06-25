package com.normalizar.domain;

import java.util.List;
import java.util.Map;

public class ConclusionesObjetoFinal {

    // Dats adicionales para S3 lambda
    private String fileName;
    private String userPoolId;
    private String tenantName;

    // Datos del reporte Original (Primera llamada) no es necesario el archivo dado
    // que es parte de otra parte del pipeline
    private String norma;
    private String activo;
    private String application;
    private String tenant;
    private String poolUserId;

    // Datos normalizados repuesta la lambda que normliza los datos (Modficar
    // lambda)
    private Map<String, Object> dataNormalizada;

    // Lista para las concluciones que hace el usuario
    private List<String> userConclusions;

    public ConclusionesObjetoFinal() {
    }


    public ConclusionesObjetoFinal(String fileName, String userPoolId, String tenantName, String norma, String activo,
            String application, String tenant, String poolUserId, Map<String, Object> dataNormalizada,
            List<String> userConclusions) {
        this.fileName = fileName;
        this.userPoolId = userPoolId;
        this.tenantName = tenantName;
        this.norma = norma;
        this.activo = activo;
        this.application = application;
        this.tenant = tenant;
        this.poolUserId = poolUserId;
        this.dataNormalizada = dataNormalizada;
        this.userConclusions = userConclusions;
    }


    public String getFileName() {
        return fileName;
    }


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getUserPoolId() {
        return userPoolId;
    }


    public void setUserPoolId(String userPoolId) {
        this.userPoolId = userPoolId;
    }


    public String getTenantName() {
        return tenantName;
    }


    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }


    public String getNorma() {
        return norma;
    }


    public void setNorma(String norma) {
        this.norma = norma;
    }


    public String getActivo() {
        return activo;
    }


    public void setActivo(String activo) {
        this.activo = activo;
    }


    public String getApplication() {
        return application;
    }


    public void setApplication(String application) {
        this.application = application;
    }


    public String getTenant() {
        return tenant;
    }


    public void setTenant(String tenant) {
        this.tenant = tenant;
    }


    public String getPoolUserId() {
        return poolUserId;
    }


    public void setPoolUserId(String poolUserId) {
        this.poolUserId = poolUserId;
    }


    public Map<String, Object> getDataNormalizada() {
        return dataNormalizada;
    }


    public void setDataNormalizada(Map<String, Object> dataNormalizada) {
        this.dataNormalizada = dataNormalizada;
    }


    public List<String> getUserConclusions() {
        return userConclusions;
    }


    public void setUserConclusions(List<String> userConclusions) {
        this.userConclusions = userConclusions;
    }


    
}
