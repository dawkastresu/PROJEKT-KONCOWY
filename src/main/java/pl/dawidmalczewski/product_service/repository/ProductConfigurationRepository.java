package pl.dawidmalczewski.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dawidmalczewski.product_service.command.CreateProductConfigCommand;
import pl.dawidmalczewski.product_service.dto.ProductConfigurationDto;
import pl.dawidmalczewski.product_service.model.ProductConfiguration;

public interface ProductConfigurationRepository extends JpaRepository <ProductConfiguration, Long> {


}
