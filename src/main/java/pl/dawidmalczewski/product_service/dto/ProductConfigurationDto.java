package pl.dawidmalczewski.product_service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.dawidmalczewski.product_service.model.ConfigurationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ProductConfigurationDto {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final ConfigurationType type;
    private final String description;
    private final String specifications;
    private final LocalDateTime createdAt;

}
