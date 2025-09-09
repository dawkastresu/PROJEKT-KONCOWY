package pl.dawidmalczewski.product_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.dawidmalczewski.product_service.command.CreateProductCommand;
import pl.dawidmalczewski.product_service.dto.ProductConfigurationDto;
import pl.dawidmalczewski.product_service.dto.ProductDto;
import pl.dawidmalczewski.product_service.model.ProductType;
import pl.dawidmalczewski.product_service.service.ProductService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Page<ProductDto> getAll(Pageable pageable) {
        log.info("GET /products called");
        Page<ProductDto> result = productService.getAll(pageable);
        log.info("Returning {} products", result.getNumberOfElements());
        return result;
    }

    @GetMapping("/{id}")
    public ProductDto findById(@PathVariable Long id) {
        log.info("GET /product called for id={}", id);
        ProductDto result = productService.findById(id);
        log.info("Product configuration returned with id={}", result.getId());
        return result;
    }

    @GetMapping("/by-type")
    public Page<ProductDto> getAllByType(@RequestParam ProductType type, Pageable pageable) {
        log.info("GET /products called with type={}", type);
        Page<ProductDto> result = productService.getByType(type, pageable);
        log.info("Returning {} products of type {}", result.getNumberOfElements(), type);
        return result;
    }

    @GetMapping("/{id}/configurations")
    public Page<ProductConfigurationDto> getProductConfigs(@PathVariable Long id, Pageable pageable) {
        log.info("GET /products/{}/configurations called", id);
        Page<ProductConfigurationDto> result = productService.getProductConfigs(id, pageable);
        log.info("Returning {} configurations for product id={}", result.getNumberOfElements(), id);
        return result;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("DELETE /products called with id={}", id);
        productService.delete(id);
        log.info("Product with id={} deleted successfully", id);
    }

    @PostMapping
    public ProductDto create(@Valid @RequestBody CreateProductCommand createProductCommand) {
        log.info("POST /products called with command={}", createProductCommand);
        ProductDto result = productService.create(createProductCommand);
        log.info("Product created with id={}", result.getId());
        return result;
    }

    @PatchMapping("/{id}")
    public ProductDto update(@PathVariable Long id, @Valid @RequestBody CreateProductCommand command) {
        log.info("PATCH /products called with id={} and command={}", id, command);
        ProductDto result = productService.updateProduct(id, command);
        log.info("Product updated with id={}", result.getId());
        return result;
    }

}
