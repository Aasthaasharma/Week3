package com.example.E_commerce.service;

import com.example.E_commerce.exception.BadRequestException;
import com.example.E_commerce.exception.ResourceNotFoundException;
import com.example.E_commerce.model.Product;
import com.example.E_commerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {


    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public Product createProduct(Product product) {
        if (product.getPrice() <= 0) {
            throw new BadRequestException("Product price must be a positive number");
        }
        return productRepository.save(product);
    }

    public Product updateProduct(int id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        if (product.getPrice() <= 0) {
            throw new BadRequestException("Product price must be a positive number");
        }
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        return productRepository.save(existingProduct);
    }

    public void deleteProduct(int id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(existingProduct);
    }

}
