package com.s3reportlambda.DtoLambda;

public class UploadResponse {
     private String message;

     public UploadResponse(String message) {
        this.message = message;
    }

     public String getMessage() {
         return message;
     }

     public UploadResponse() {
    }

     public void setMessage(String message) {
         this.message = message;
     }


}
