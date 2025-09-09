package pl.dawidmalczewski.product_service.exception;

import org.springframework.http.HttpStatus;

public class UnsupportedProductTypeException extends ProductServiceException {

    public UnsupportedProductTypeException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
