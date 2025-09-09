package pl.dawidmalczewski.product_service.utils;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.dawidmalczewski.product_service.exception.InvalidConfigurationException;
import pl.dawidmalczewski.product_service.model.ProductType;
import pl.dawidmalczewski.product_service.model.ProductConfiguration;

import java.util.Set;

@Component
public class ConfigurationValidator {

    public void validateConfigurationsAllowed(ProductType type, Set<ProductConfiguration> configs) {
        if (configs != null && !configs.isEmpty()) {
            if (!(type == ProductType.SMARTPHONE || type == ProductType.COMPUTER)) {
                throw new InvalidConfigurationException(
                        "Configurations are only allowed for products of type SMARTPHONE or COMPUTER."
                        , HttpStatus.BAD_REQUEST
                );
            }
        }
    }

}
