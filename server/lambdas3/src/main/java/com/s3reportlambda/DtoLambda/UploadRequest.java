package com.s3reportlambda.DtoLambda;

import java.util.List;

public class UploadRequest {

    public UploadRequest(String userPoolId, String tenantName, List<FilePayLoad> files) {
        this.userPoolId = userPoolId;
        this.tenantName = tenantName;
        this.files = files;
    }

    private String userPoolId;
    private String tenantName;
    private List<FilePayLoad> files;

    public UploadRequest() {
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


    public List<FilePayLoad> getFiles() {
        return files;
    }

    public void setFiles(List<FilePayLoad> files) {
        this.files = files;
    }

    public static class FilePayLoad {
    private String  fileName;
    private String  base64;

    public FilePayLoad(){
        
    }
    public FilePayLoad(String fileName, String base64) {
        this.fileName = fileName;
        this.base64 = base64;
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getBase64() {
        return base64;
    }
    public void setBase64(String base64) {
        this.base64 = base64;
    }

    

    
}


}
