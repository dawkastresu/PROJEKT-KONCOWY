package pl.dawidmalczewski.product_service.exception;

import org.springframework.http.HttpStatus;

public class ProductAlreadyExistsException extends ProductServiceException {

    public ProductAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

}
