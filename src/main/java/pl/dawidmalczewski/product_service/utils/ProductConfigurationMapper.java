package pl.dawidmalczewski.product_service.utils;

import org.mapstruct.Mapper;
import pl.dawidmalczewski.product_service.command.CreateProductConfigCommand;
import pl.dawidmalczewski.product_service.dto.ProductConfigurationDto;
import pl.dawidmalczewski.product_service.model.ProductConfiguration;

@Mapper(componentModel = "spring")
public interface ProductConfigurationMapper {

    ProductConfigurationDto toDto(ProductConfiguration productConfiguration);
    ProductConfiguration toEntity(ProductConfigurationDto productConfigurationDto);
    ProductConfiguration toEntity(CreateProductConfigCommand createProductConfigCommand);

}
