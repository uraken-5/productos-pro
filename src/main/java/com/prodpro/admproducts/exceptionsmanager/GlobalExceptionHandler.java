package com.prodpro.admproducts.exceptionsmanager;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.prodpro.admproducts.exceptionsmanager.exceptions.ErrorDetails;
import com.prodpro.admproducts.exceptionsmanager.exceptions.GeneralException;
import com.prodpro.admproducts.exceptionsmanager.exceptions.ProductNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	 @ExceptionHandler(ProductNotFoundException.class)
	    public ResponseEntity<?> handleProductoNotFoundException(ProductNotFoundException ex) {
	        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.NOT_FOUND.value(), ex.getMessage());
	        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	    }

	    @ExceptionHandler(GeneralException.class)
	    public ResponseEntity<?> handleGeneralException(GeneralException ex) {
	        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
	        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	    }

	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<?> handleGlobalException(Exception ex) {
	        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
	        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
}
