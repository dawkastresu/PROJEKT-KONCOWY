package pl.dawidmalczewski.product_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.dawidmalczewski.product_service.model.Product;
import pl.dawidmalczewski.product_service.model.ProductType;

public interface ProductRepository extends JpaRepository <Product, Long> {

    Page<Product> findAllByType(ProductType type, Pageable pageable);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}
