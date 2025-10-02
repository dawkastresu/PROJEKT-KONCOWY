package pl.dawidmalczewski.product_service.command;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.dawidmalczewski.product_service.model.ConfigurationType;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class CreateProductConfigCommand {

    @Size(max = 20, message = "Name cannot exceed 20 characters")
    private final String name;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Price format is invalid")
    private final BigDecimal price;

    private final ConfigurationType type;

    @Min(value = 0, message = "Quantity cannot be negative")
    private final Long quantity;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private final String description;

    @Size(max = 1000, message = "Specification cannot exceed 1000 characters")
    private final String specification;

    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    @Pattern(regexp = "^https?://.*\\.(jpg|jpeg|png|gif|webp)$",
            message = "Image URL must be a valid HTTP/HTTPS URL ending with jpg, jpeg, png, gif, or webp",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private final String imageUrl;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private final String specifications;

}
