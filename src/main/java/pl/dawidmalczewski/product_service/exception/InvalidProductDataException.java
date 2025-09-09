package pl.dawidmalczewski.product_service.exception;

import org.springframework.http.HttpStatus;

public class InvalidProductDataException extends ProductServiceException {

    public InvalidProductDataException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
