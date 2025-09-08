package com.bistral.app.bistral_order_service.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    private  String resourceName;
    private  int code;

    public ResourceNotFoundException(String resourceName, int code, String message) {
        super(message);
        this.resourceName = resourceName;
        this.code = code;
    }

    public ResourceNotFoundException(String resourceName, String message){
        super(message);
        this.resourceName=resourceName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public int getCode() {
        return code;
    }
}
