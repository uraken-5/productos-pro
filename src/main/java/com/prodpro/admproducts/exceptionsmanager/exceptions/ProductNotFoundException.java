package com.prodpro.admproducts.exceptionsmanager.exceptions;


public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product not found: " + id);
    }
}
