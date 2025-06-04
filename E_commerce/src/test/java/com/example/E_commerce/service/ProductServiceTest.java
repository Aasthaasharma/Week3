package com.example.E_commerce.service;

import com.example.E_commerce.model.Product;
import com.example.E_commerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    private ProductService productService;
    AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        this.productService = new ProductService(this.productRepository);
    }

    @Test
    void getAllProducts() {
        productService.getAllProducts();
        verify(productRepository).findAll();
    }

    @Test
    void getProductById() {
        int productId = 1;
        Product product = new Product(productId, "Test", "Test desc", 10.0);
        when(productRepository.findById(productId)).thenReturn(java.util.Optional.of(product));
        Product foundProduct = productService.getProductById(productId);
        verify(productRepository).findById(productId);
        assertEquals(product, foundProduct);
    }

    @Test
    void createProduct() {
        Product newProduct = new Product(8, "Nivea", "Nivea cream for skin", 100.00);
        when(productRepository.save(newProduct)).thenReturn(newProduct);
        Product createdProduct = productService.createProduct(newProduct);
        verify(productRepository).save(newProduct);
        assertEquals(newProduct, createdProduct);
    }

    @Test
    void updateProduct() {
        int productId = 8;
        Product updatedProduct = new Product(productId, "Nivea", "Nivea cream for skin", 150.00);
        when(productRepository.findById(productId)).thenReturn(java.util.Optional.of(updatedProduct));
        productService.updateProduct(productId, updatedProduct);
        verify(productRepository).findById(productId);
        verify(productRepository).save(updatedProduct);
    }

    @Test
    void deleteProduct() {
        int productId = 8;
        Product existingProduct = new Product(productId, "Nivea", "Nivea cream for skin", 100.00);
        when(productRepository.findById(productId)).thenReturn(java.util.Optional.of(existingProduct));
        productService.deleteProduct(productId);
        verify(productRepository).findById(productId);
        verify(productRepository).delete(existingProduct); // verify delete(entity), not deleteById
    }
}