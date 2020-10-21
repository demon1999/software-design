package ru.demon1999.sd.mvc.dao;

import ru.demon1999.sd.mvc.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * @author akirakozov
 */
public interface ProductDao {
    int addProduct(Product product);

    List<Product> getProducts();

    Optional<Product> getProductWithMaxPrice();
    Optional<Product> getProductWithMinPrice();
}
