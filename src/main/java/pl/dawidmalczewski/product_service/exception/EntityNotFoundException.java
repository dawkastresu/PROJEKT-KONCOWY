package pl.dawidmalczewski.product_service.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends ProductServiceException {

    public EntityNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

}
