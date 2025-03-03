package com.app.foodapp.services;

import com.app.foodapp.models.Product;
import com.app.foodapp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(Product product) {
        return this.productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return this.productRepository.findById(id);
    }

    public void deleteProductById(Long id) {
        Product product = this.productRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Product doesn't exist")
        );
        this.productRepository.delete(product);
    }

}
