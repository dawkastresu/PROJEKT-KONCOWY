package pl.dawidmalczewski.product_service.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.dawidmalczewski.product_service.command.CreateProductCommand;
import pl.dawidmalczewski.product_service.dto.ProductDto;
import pl.dawidmalczewski.product_service.model.Product;

@Mapper(componentModel = "spring", uses = {ProductConfigurationMapper.class})
public interface ProductMapper {

    ProductDto toDto(Product product);
    Product toEntity(ProductDto productDto);

    @Mapping(target = "availableConfigurations", ignore = true)
    Product toEntity(CreateProductCommand command);

}
