package pl.dawidmalczewski.product_service.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.dawidmalczewski.product_service.command.CreateProductCommand;
import pl.dawidmalczewski.product_service.command.CreateProductConfigCommand;
import pl.dawidmalczewski.product_service.exception.InvalidProductDataException;
import pl.dawidmalczewski.product_service.exception.ProductAlreadyExistsException;
import pl.dawidmalczewski.product_service.model.Product;
import pl.dawidmalczewski.product_service.model.ProductConfiguration;
import pl.dawidmalczewski.product_service.repository.ProductConfigurationRepository;
import pl.dawidmalczewski.product_service.repository.ProductRepository;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateDataValidator {

    private final ProductRepository productRepository;
    private final ProductConfigurationRepository productConfigurationRepository;

    public void validateUpdateData(CreateProductCommand command, Product existingProduct) {
        if (command.getName() != null && command.getName().trim().isEmpty()) {
            log.warn("Attempted to update product with empty name");
            throw new InvalidProductDataException("Product name cannot be empty");
        }

        if (command.getName() != null && !command.getName().equals(existingProduct.getName())) {
            if (productRepository.existsByName(command.getName())) {
                log.warn("Attempted to update product to duplicate name: {}", command.getName());
                throw new ProductAlreadyExistsException("Product with name '" + command.getName() + "' already exists");
            }
        }
    }

    public void validateUpdateData(CreateProductConfigCommand command, ProductConfiguration existingProductConfiguration) {
        if (command.getName() != null && command.getName().trim().isEmpty()) {
            log.warn("Attempted to update product configuration with empty name");
            throw new InvalidProductDataException("Product configuration name cannot be empty");
        }

        if (command.getName() != null && !command.getName().equals(existingProductConfiguration.getName())) {
            if (productRepository.existsByName(command.getName())) {
                log.warn("Attempted to update product configuration to duplicate name: {}", command.getName());
                throw new ProductAlreadyExistsException("Product configuration with name '" + command.getName() + "' already exists");
            }
        }
    }

}
