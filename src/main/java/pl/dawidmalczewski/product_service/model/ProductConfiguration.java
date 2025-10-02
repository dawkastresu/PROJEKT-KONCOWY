package pl.dawidmalczewski.product_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import pl.dawidmalczewski.product_service.command.CreateProductCommand;
import pl.dawidmalczewski.product_service.command.CreateProductConfigCommand;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "product_configurations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private ConfigurationType type;

    @Column
    private Long quantity;

    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String specifications;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public void update(CreateProductConfigCommand command) {
        if (command.getName() != null) this.setName(command.getName());
        if (command.getPrice() != null) this.setPrice(command.getPrice());
        if (command.getType() != null) this.setType(command.getType());
        if (command.getQuantity() != null) this.setQuantity(command.getQuantity());
        if (command.getDescription() != null) this.setDescription(command.getDescription());
        if (command.getSpecification() != null) this.setSpecifications(command.getSpecification());
        if (command.getImageUrl() != null) this.setImageUrl(command.getImageUrl());
    }

}