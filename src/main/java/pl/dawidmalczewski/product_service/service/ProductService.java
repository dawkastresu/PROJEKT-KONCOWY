package pl.dawidmalczewski.product_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.dawidmalczewski.product_service.command.CreateProductCommand;
import pl.dawidmalczewski.product_service.dto.ProductConfigurationDto;
import pl.dawidmalczewski.product_service.dto.ProductDto;
import pl.dawidmalczewski.product_service.exception.*;
import pl.dawidmalczewski.product_service.model.Product;
import pl.dawidmalczewski.product_service.model.ProductConfiguration;
import pl.dawidmalczewski.product_service.model.ProductType;
import pl.dawidmalczewski.product_service.repository.ProductConfigurationRepository;
import pl.dawidmalczewski.product_service.repository.ProductRepository;
import pl.dawidmalczewski.product_service.utils.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductConfigurationRepository productConfigurationRepository;
    private final ProductMapper productMapper;
    private final ProductConfigurationMapper productConfigurationMapper;
    private final ConfigurationValidator configurationValidator;
    private final ProductDataValidator productDataValidator;
    private final UpdateDataValidator updateDataValidator;

    public Page<ProductDto> getAll(Pageable pageable) {
        log.debug("Fetching all products with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Product> page = productRepository.findAll(pageable);
        log.debug("Found {} products", page.getTotalElements());
        return page.map(productMapper::toDto);
    }

    public ProductDto findById(Long id) {
        if (id == null || id <= 0) {
            log.warn("Attempted to find product with invalid ID: {}", id);
            throw new InvalidConfigurationException("Product ID must be a positive number", HttpStatus.BAD_REQUEST);
        }

        log.debug("Fetching product with ID: {}", id);
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("Product not found with ID: {}", id);
                    return new EntityNotFoundException("Product not found with id: " + id);
                });
    }

    public Page<ProductDto> getByType(ProductType type, Pageable pageable) {
        if (type == null) {
            log.warn("Attempted to fetch products with null type");
            throw new InvalidProductDataException("Product type cannot be null");
        }

        log.debug("Fetching products by type: {} with pagination: page={}, size={}", type, pageable.getPageNumber(), pageable.getPageSize());
        Page<Product> page = productRepository.findAllByType(type, pageable);
        log.debug("Found {} products of type {}", page.getTotalElements(), type);
        return page.map(productMapper::toDto);
    }

    public void delete(Long id) {
        if (id == null || id <= 0) {
            log.warn("Attempted to delete product with invalid ID: {}", id);
            throw new InvalidProductDataException("Product ID must be a positive number");
        }

        if (!productRepository.existsById(id)) {
            log.warn("Attempted to delete non-existent product with ID: {}", id);
            throw new EntityNotFoundException("Product not found with id: " + id);
        }

        log.info("Deleting product with ID: {}", id);
        productRepository.deleteById(id);
        log.info("Successfully deleted product with ID: {}", id);
    }

    public ProductDto create(CreateProductCommand command) {
        log.info("Creating new product: {}", command.getName());

        // Walidacja podstawowych danych
        productDataValidator.validateProductData(command);

        // Sprawdzenie duplikatów
        if (productRepository.existsByName(command.getName())) {
            log.warn("Attempted to create product with duplicate name: {}", command.getName());
            throw new ProductAlreadyExistsException("Product with name '" + command.getName() + "' already exists");
        }

        Product product = productMapper.toEntity(command);

        if (command.getConfigurationIds() != null && !command.getConfigurationIds().isEmpty()) {
            log.debug("Processing {} configurations for new product", command.getConfigurationIds().size());
            Set<ProductConfiguration> configs = validateAndGetConfigurations(command.getConfigurationIds(), product.getType());
            product.setAvailableConfigurations(configs);
        }

        Product saved = productRepository.save(product);
        log.info("Successfully created product with ID: {} and name: {}", saved.getId(), saved.getName());
        return productMapper.toDto(saved);
    }

    public Page<ProductConfigurationDto> getProductConfigs(Long id, Pageable pageable) {
        if (id == null || id <= 0) {
            log.warn("Attempted to get configurations for invalid product ID: {}", id);
            throw new InvalidProductDataException("Product ID must be a positive number");
        }

        log.debug("Fetching configurations for product ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found with ID: {}", id);
                    return new EntityNotFoundException("Product not found with id: " + id);
                });

        if (product.getType() != ProductType.SMARTPHONE && product.getType() != ProductType.COMPUTER) {
            log.warn("Attempted to get configurations for unsupported product type: {}", product.getType());
            throw new UnsupportedProductTypeException("This product type does not support configurations");
        }

        List<ProductConfigurationDto> configs = product.getAvailableConfigurations()
                .stream()
                .map(productConfigurationMapper::toDto)
                .toList();

        log.debug("Found {} configurations for product ID: {}", configs.size(), id);
        return new PageImpl<>(
                configs.subList((int) pageable.getOffset(),
                        Math.min((int) pageable.getOffset() + pageable.getPageSize(), configs.size())),
                pageable,
                configs.size()
        );
    }

    public ProductDto updateProduct(Long id, CreateProductCommand command) {
        if (id == null || id <= 0) {
            log.warn("Attempted to update product with invalid ID: {}", id);
            throw new InvalidProductDataException("Product ID must be a positive number");
        }

        log.info("Updating product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found for update with ID: {}", id);
                    return new EntityNotFoundException("Product not found with id: " + id);
                });

        // Walidacja danych do aktualizacji
        updateDataValidator.validateUpdateData(command, product);

        Set<ProductConfiguration> configs = null;
        if (command.getConfigurationIds() != null) {
            log.debug("Processing {} configurations for product update", command.getConfigurationIds().size());
            ProductType effectiveType = command.getType() != null ? command.getType() : product.getType();
            configs = validateAndGetConfigurations(command.getConfigurationIds(), effectiveType);
        }

        product.update(command, configs);
        Product saved = productRepository.save(product);
        log.info("Successfully updated product with ID: {}", id);
        return productMapper.toDto(saved);
    }


    //Walidacja konfiguracji
    private Set<ProductConfiguration> validateAndGetConfigurations(Set<Long> configIds, ProductType productType) {
        if (configIds.isEmpty()) {
            return new HashSet<>();
        }

        Set<ProductConfiguration> configs = new HashSet<>(
                productConfigurationRepository.findAllById(configIds));

        // Sprawdzenie czy wszystkie konfiguracje zostały znalezione
        if (configs.size() != configIds.size()) {
            List<Long> foundIds = configs.stream()
                    .map(ProductConfiguration::getId)
                    .toList();
            List<Long> missingIds = configIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            log.warn("Configuration(s) not found with IDs: {}", missingIds);
            throw new EntityNotFoundException("Configuration(s) not found with IDs: " + missingIds);
        }

        // Walidacja zgodności konfiguracji z typem produktu
        try {
            configurationValidator.validateConfigurationsAllowed(productType, configs);
            log.debug("All configurations validated successfully for product type: {}", productType);
        } catch (Exception ex) {
            log.warn("Configuration validation failed for product type {}: {}", productType, ex.getMessage());
            throw new InvalidConfigurationException("Invalid configuration for product type: " + productType, HttpStatus.BAD_REQUEST);
        }

        return configs;
    }
}