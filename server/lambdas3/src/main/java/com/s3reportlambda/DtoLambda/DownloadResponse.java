package com.s3reportlambda.DtoLambda;

public class DownloadResponse {
    private String fileName;
    private String base64File;

    public DownloadResponse(String base64File) {
        this.base64File = base64File;
    }

    public DownloadResponse(String fileName, String base64File) {
        this.fileName = fileName;
        this.base64File = base64File;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBase64File() {
        return base64File;
    }

    public void setBase64File(String base64File) {
        this.base64File = base64File;
    }
}
