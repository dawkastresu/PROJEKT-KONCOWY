package pl.dawidmalczewski.product_service.dto;

import lombok.*;
import pl.dawidmalczewski.product_service.model.ProductConfiguration;
import pl.dawidmalczewski.product_service.model.ProductType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
public class ProductDto {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final ProductType type;
    private final Long quantity;
    private final String description;
    private final String imageUrl;
    private final Set<ProductConfigurationDto> availableConfigurations;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

}
