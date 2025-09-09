package pl.dawidmalczewski.product_service.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.dawidmalczewski.product_service.command.CreateProductCommand;
import pl.dawidmalczewski.product_service.exception.InvalidProductDataException;

import java.math.BigDecimal;

@Slf4j
@Component
public class ProductDataValidator {

    public void validateProductData(CreateProductCommand command) {
        if (command.getName() == null || command.getName().trim().isEmpty()) {
            log.warn("Attempted to create product with empty name");
            throw new InvalidProductDataException("Product name cannot be empty");
        }

        if (command.getType() == null) {
            log.warn("Attempted to create product without type");
            throw new InvalidProductDataException("Product type is required");
        }

        if (command.getPrice() != null && command.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("Attempted to create product with negative price: {}", command.getPrice());
            throw new InvalidProductDataException("Product price cannot be negative");
        }
    }

}
