package pl.dawidmalczewski.product_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.dawidmalczewski.product_service.command.CreateProductConfigCommand;
import pl.dawidmalczewski.product_service.dto.ProductConfigurationDto;
import pl.dawidmalczewski.product_service.exception.EntityNotFoundException;
import pl.dawidmalczewski.product_service.exception.InvalidConfigurationException;
import pl.dawidmalczewski.product_service.model.ProductConfiguration;
import pl.dawidmalczewski.product_service.repository.ProductConfigurationRepository;
import pl.dawidmalczewski.product_service.utils.ProductConfigurationMapper;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductConfigurationService {

    private final ProductConfigurationRepository repository;
    private final ProductConfigurationMapper productConfigurationMapper;

    public Page<ProductConfigurationDto> getAll(Pageable pageable) {
        log.debug("Fetching all product configurations with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<ProductConfiguration> page = repository.findAll(pageable);
        log.debug("Found {} product configurations", page.getTotalElements());
        return page.map(productConfigurationMapper::toDto);
    }

    public ProductConfigurationDto findById(Long id) {
        if (id == null || id <= 0) {
            log.warn("Attempted to find product configuration with invalid ID: {}", id);
            throw new InvalidConfigurationException("Product configuration ID must be a positive number", HttpStatus.BAD_REQUEST);
        }

        log.debug("Fetching product configuration with ID: {}", id);
        return repository.findById(id)
                .map(productConfigurationMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("Product configuration not found with ID: {}", id);
                    return new EntityNotFoundException("Product configuration not found with id: " + id);
                });
    }

    public ProductConfigurationDto create(CreateProductConfigCommand createProductConfigCommand) {
        log.info("Creating new product configuration: {}", createProductConfigCommand);
        ProductConfiguration entity = productConfigurationMapper.toEntity(createProductConfigCommand);
        repository.save(entity);
        log.info("Successfully created product configuration with id: {}", entity.getId());
        return productConfigurationMapper.toDto(entity);
    }

    public void delete(Long id) {
        log.info("Deleting product configuration with id: {}", id);
        repository.deleteById(id);
        log.info("Deleted product configuration with id: {}", id);
    }

}
