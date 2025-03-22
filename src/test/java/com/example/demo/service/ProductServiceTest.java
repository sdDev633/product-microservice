package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product product2;
    private List<Product> productList;

    @BeforeEach
    void setUp() {
        product1 = new Product("Laptop", 1299.99);
        product1.setId(1L);

        product2 = new Product("Phone", 799.99);
        product2.setId(2L);

        productList = Arrays.asList(product1, product2);
    }

    @Test
    void testCreateProduct_Success() {
        // Arrange
        Product newProduct = new Product("New Product", 499.99);
        when(productRepository.save(any(Product.class))).thenReturn(newProduct);

        // Act
        String result = productService.createProduct(newProduct);

        // Assert
        assertEquals("Product created successfully", result);
        verify(productRepository, times(1)).save(newProduct);
    }

    @Test
    void testGetAllProducts_WithProducts() {
        // Arrange
        when(productRepository.findAll()).thenReturn(productList);

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertEquals(2, result.size());
        assertEquals(product1, result.get(0));
        assertEquals(product2, result.get(1));
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetAllProducts_EmptyList() {
        // Arrange
        when(productRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertEquals(0, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById_ExistingProduct() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        // Act
        Product result = productService.getProductById(1L);

        // Assert
        assertEquals(product1, result);
        assertEquals("Laptop", result.getName());
        assertEquals(1299.99, result.getPrice());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductById_NonExistingProduct() {
        // Arrange
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Product result = productService.getProductById(99L);

        // Assert
        assertNull(result);
        verify(productRepository, times(1)).findById(99L);
    }

    @Test
    void testUpdateProduct_ExistingProduct() {
        // Arrange
        Product updatedProduct = new Product("Updated Laptop", 1499.99);

        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        // Act
        String result = productService.updateProduct(1L, updatedProduct);

        // Assert
        assertEquals("Product updated successfully", result);
        assertEquals("Updated Laptop", product1.getName());
        assertEquals(1499.99, product1.getPrice());
        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(product1);
    }

    @Test
    void testUpdateProduct_NonExistingProduct() {
        // Arrange
        Product updatedProduct = new Product("Updated Laptop", 1499.99);
        when(productRepository.existsById(99L)).thenReturn(false);

        // Act
        String result = productService.updateProduct(99L, updatedProduct);

        // Assert
        assertEquals("Product not found", result);
        verify(productRepository, times(1)).existsById(99L);
        verify(productRepository, never()).findById(99L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_NullFields() {
        // Arrange
        Product existingProduct = new Product("Existing Product", 999.99);
        existingProduct.setId(1L);

        Product partialUpdate = new Product(null, null);

        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        // Act
        String result = productService.updateProduct(1L, partialUpdate);

        // Assert
        assertEquals("Product updated successfully", result);
        assertNull(existingProduct.getName());
        assertNull(existingProduct.getPrice());
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    void testDeleteProduct_ExistingProduct() {
        // Arrange
        when(productRepository.existsById(1L)).thenReturn(true);

        // Act
        String result = productService.deleteProduct(1L);

        // Assert
        assertEquals("Product has been deleted successfully", result);
        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProduct_NonExistingProduct() {
        // Arrange
        when(productRepository.existsById(99L)).thenReturn(false);

        // Act
        String result = productService.deleteProduct(99L);

        // Assert
        assertNull(result);
        verify(productRepository, times(1)).existsById(99L);
        verify(productRepository, never()).deleteById(99L);
    }

    @Test
    void testUpdateProduct_IdMismatch() {
        // Arrange
        Product updatedProduct = new Product("Updated Product", 1499.99);
        updatedProduct.setId(2L); // Different from the ID in the path parameter

        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        // Act
        String result = productService.updateProduct(1L, updatedProduct);

        // Assert
        assertEquals("Product updated successfully", result);
        // Verify that the service uses the path parameter ID (1L) not the product ID (2L)
        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateProduct_WithId() {
        // Arrange
        Product productWithId = new Product("New Product", 499.99);
        productWithId.setId(10L);

        when(productRepository.save(any(Product.class))).thenReturn(productWithId);

        // Act
        String result = productService.createProduct(productWithId);

        // Assert
        assertEquals("Product created successfully", result);
        verify(productRepository, times(1)).save(productWithId);
        // In a real application, you might want to ignore the provided ID for create operations
    }
}