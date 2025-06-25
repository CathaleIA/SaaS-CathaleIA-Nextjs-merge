package com.s3reportlambda.domain;

public class S3FileInfo {
    private String key;
    private Long size;
    private String lastModified;
    
    public S3FileInfo(String key, Long size, String lastModified) {
        this.key = key;
        this.size = size;
        this.lastModified = lastModified;
    }
    public S3FileInfo() {
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public Long getSize() {
        return size;
    }
    public void setSize(Long size) {
        this.size = size;
    }
    public String getLastModified() {
        return lastModified;
    }
    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }
}
