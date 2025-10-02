package pl.dawidmalczewski.product_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.dawidmalczewski.product_service.dto.ProductDto;

import java.util.UUID;

@FeignClient(name = "product-service", url = "${services.product.url}")
public interface ProductClient {

    @GetMapping("/products/{id}")
    ProductDto getProductById(@PathVariable("id") UUID id);

}
