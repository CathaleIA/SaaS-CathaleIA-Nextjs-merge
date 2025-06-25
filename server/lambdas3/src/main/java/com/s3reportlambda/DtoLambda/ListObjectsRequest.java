package com.s3reportlambda.DtoLambda;

public class ListObjectsRequest {
    private String tenantName;
    private String userPoolid;
    public ListObjectsRequest() {
    }
    public String getTenantName() {
        return tenantName;
    }
    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }
    public String getUserPoolid() {
        return userPoolid;
    }
    public void setUserPoolid(String userPoolid) {
        this.userPoolid = userPoolid;
    }

    
}
