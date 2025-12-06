package com.bistral.app.bistral_order_service.exceptions;

public class CloseOrderException extends Exception {
    int errorCode;

    public CloseOrderException(String msg, int code) {
        super(msg);
        this.errorCode = code;
    }
}
