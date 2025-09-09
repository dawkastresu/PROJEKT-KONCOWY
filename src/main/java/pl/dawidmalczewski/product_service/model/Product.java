package pl.dawidmalczewski.product_service.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pl.dawidmalczewski.product_service.command.CreateProductCommand;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType type;

    @Column
    private Long quantity;

    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    // Tabela asocjacyjna
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_product_configuration",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "product_configuration_id")
    )

    private Set<ProductConfiguration> availableConfigurations = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void update(CreateProductCommand command, Set<ProductConfiguration> configs) {
        if (command.getName() != null) this.setName(command.getName());
        if (command.getPrice() != null) this.setPrice(command.getPrice());
        if (command.getType() != null) this.setType(command.getType());
        if (command.getQuantity() != null) this.setQuantity(command.getQuantity());
        if (command.getDescription() != null) this.setDescription(command.getDescription());
        if (command.getImageUrl() != null) this.setImageUrl(command.getImageUrl());
        if (configs != null) this.setAvailableConfigurations(configs);
    }

}
