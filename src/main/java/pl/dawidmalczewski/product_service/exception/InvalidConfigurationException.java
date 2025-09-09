package pl.dawidmalczewski.product_service.exception;

import org.springframework.http.HttpStatus;

public class InvalidConfigurationException extends ProductServiceException {

    public InvalidConfigurationException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

}
