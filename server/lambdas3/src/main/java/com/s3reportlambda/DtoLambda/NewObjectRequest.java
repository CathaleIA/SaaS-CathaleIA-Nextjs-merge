package com.s3reportlambda.DtoLambda;

public class NewObjectRequest {
    private String userPoolId;
    private String tenantName;
    private String fileName;

    private String fileBase64;

    public NewObjectRequest() {
    }

    public NewObjectRequest(String userPoolId, String tenantName, String fileName, String fileBase64) {
        this.userPoolId = userPoolId;
        this.tenantName = tenantName;
        this.fileName = fileName;
        this.fileBase64 = fileBase64;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileBase64() {
        return fileBase64;
    }

    public void setFileBase64(String fileBase64) {
        this.fileBase64 = fileBase64;
    }
}
