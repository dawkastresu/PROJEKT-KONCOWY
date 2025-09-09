package pl.dawidmalczewski.product_service.command;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.dawidmalczewski.product_service.dto.ProductConfigurationDto;
import pl.dawidmalczewski.product_service.model.ProductType;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public class CreateProductCommand {

    @Size(max = 20, message = "Product name cannot exceed 20 characters")
    private final String name;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Price format is invalid")
    private final BigDecimal price;

    private final ProductType type;

    @Min(value = 0, message = "Quantity cannot be negative")
    private final Long quantity;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private final String description;

    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    @Pattern(regexp = "^https?://.*\\.(jpg|jpeg|png|gif|webp)$",
            message = "Image URL must be a valid HTTP/HTTPS URL ending with jpg, jpeg, png, gif, or webp",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private final String imageUrl;

    @Size(max = 50, message = "Cannot assign more than 50 configurations")
    private final Set<Long> configurationIds;

}