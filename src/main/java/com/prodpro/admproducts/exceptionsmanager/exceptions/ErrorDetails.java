package com.prodpro.admproducts.exceptionsmanager.exceptions;

import lombok.Data;

@Data
public class ErrorDetails {
    private int statusCode;
    private String message;

    public ErrorDetails(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

}
