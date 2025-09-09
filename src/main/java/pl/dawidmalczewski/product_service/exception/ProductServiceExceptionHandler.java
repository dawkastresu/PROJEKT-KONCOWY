package pl.dawidmalczewski.product_service.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@ControllerAdvice
public class ProductServiceExceptionHandler {

    // Twoje istniejące handlery...
    @ExceptionHandler(InvalidConfigurationException.class)
    protected ResponseEntity<ErrorMessage> handleInvalidConfigurationException(InvalidConfigurationException ex) {
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage(), ex.getHttpStatus()), new HttpHeaders(), ex.getHttpStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorMessage> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage(), ex.getHttpStatus()), new HttpHeaders(), ex.getHttpStatus());
    }

    // NOWE HANDLERY - dodaj te:

    // Spring Validation - 400 Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorMessage> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                new ErrorMessage(message, HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }

    // Baza danych - 409 Conflict
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ErrorMessage> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return new ResponseEntity<>(
                new ErrorMessage("Data conflict - record already exists or violates constraints", HttpStatus.CONFLICT),
                HttpStatus.CONFLICT
        );
    }

    // Nowe wyjątki biznesowe
    @ExceptionHandler(ProductAlreadyExistsException.class)
    protected ResponseEntity<ErrorMessage> handleProductAlreadyExists(ProductAlreadyExistsException ex) {
        return new ResponseEntity<>(
                new ErrorMessage(ex.getMessage(), ex.getHttpStatus()),
                ex.getHttpStatus()
        );
    }

    @ExceptionHandler(InvalidProductDataException.class)
    protected ResponseEntity<ErrorMessage> handleInvalidProductData(InvalidProductDataException ex) {
        return new ResponseEntity<>(
                new ErrorMessage(ex.getMessage(), ex.getHttpStatus()),
                ex.getHttpStatus()
        );
    }

    // Błędny typ parametru (np. String zamiast Long w URL)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorMessage> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "Invalid value '" + ex.getValue() + "' for parameter '" + ex.getName() + "'";
        return new ResponseEntity<>(
                new ErrorMessage(message, HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }

    // Ogólny fallback
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorMessage> handleGenericException(Exception ex) {
        return new ResponseEntity<>(
                new ErrorMessage("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
