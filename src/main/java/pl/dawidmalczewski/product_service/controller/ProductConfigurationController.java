package pl.dawidmalczewski.product_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import pl.dawidmalczewski.product_service.command.CreateProductConfigCommand;
import pl.dawidmalczewski.product_service.dto.ProductConfigurationDto;
import pl.dawidmalczewski.product_service.exception.EntityNotFoundException;
import pl.dawidmalczewski.product_service.exception.InvalidProductDataException;
import pl.dawidmalczewski.product_service.model.Product;
import pl.dawidmalczewski.product_service.model.ProductConfiguration;
import pl.dawidmalczewski.product_service.repository.ProductConfigurationRepository;
import pl.dawidmalczewski.product_service.service.ProductConfigurationService;
import pl.dawidmalczewski.product_service.utils.ProductConfigurationMapper;
import pl.dawidmalczewski.product_service.utils.UpdateDataValidator;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/product_configurations")
public class ProductConfigurationController {

    private final ProductConfigurationService productConfigurationService;
    private final ProductConfigurationRepository productConfigurationRepository;
    private final UpdateDataValidator updateDataValidator;
    private final ProductConfigurationMapper productConfigurationMapper;

    @GetMapping
    public Page<ProductConfigurationDto> getAll(Pageable pageable) {
        log.info("GET /product_configurations called");
        Page<ProductConfigurationDto> result = productConfigurationService.getAll(pageable);
        log.info("Returning {} product configurations", result.getNumberOfElements());
        return result;
    }

    @GetMapping("/{id}")
    public ProductConfigurationDto findById(@PathVariable Long id) {
        log.info("GET /product_configurations called for id={}", id);
        ProductConfigurationDto result = productConfigurationService.findById(id);
        log.info("Product configuration returned with id={}", result.getId());
        return result;
    }

    @PostMapping
    public ProductConfigurationDto create(@Valid @RequestBody CreateProductConfigCommand command) {
        log.info("POST /product_configurations called with command={}", command);
        ProductConfigurationDto result = productConfigurationService.create(command);
        log.info("Product configuration created with id={}", result.getId());
        return result;
    }

    @PatchMapping
    public ProductConfigurationDto update(@PathVariable Long id, @Valid @RequestBody CreateProductConfigCommand command) {
        if (id == null || id <= 0) {
            log.warn("Attempted to update product configuration with invalid ID: {}", id);
            throw new InvalidProductDataException("Product Configuration ID must be a positive number");
        }

        log.info("Updating product configuration with ID: {}", id);
        ProductConfiguration productConfig = productConfigurationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product configuration not found for update with ID: {}", id);
                    return new EntityNotFoundException("Product configuration not found with id: " + id);
                });

        updateDataValidator.validateUpdateData(command, productConfig);

        productConfig.update(command);
        ProductConfiguration saved = productConfigurationRepository.save(productConfig);
        log.info("Successfully updated product configuration with ID: {}", id);
        return productConfigurationMapper.toDto(saved);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("DELETE /product_configurations/{} called", id);
        productConfigurationService.delete(id);
        log.info("Product configuration with id={} deleted successfully", id);
    }

}
