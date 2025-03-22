package com.example.demo.repository;

import com.example.demo.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testSaveProduct() {
        // Given
        Product product = new Product("Test Product", 99.99);

        // When
        Product savedProduct = productRepository.save(product);

        // Then
        assertNotNull(savedProduct.getId());
        assertEquals("Test Product", savedProduct.getName());
        assertEquals(99.99, savedProduct.getPrice());
    }

    @Test
    public void testFindById() {
        // Given
        Product product = new Product("Test Product", 49.99);
        entityManager.persist(product);
        entityManager.flush();

        // When
        Optional<Product> foundProduct = productRepository.findById(product.getId());

        // Then
        assertTrue(foundProduct.isPresent());
        assertEquals("Test Product", foundProduct.get().getName());
        assertEquals(49.99, foundProduct.get().getPrice());
    }

    @Test
    public void testFindAll() {
        // Given
        Product product1 = new Product("Product 1", 29.99);
        Product product2 = new Product("Product 2", 39.99);
        entityManager.persist(product1);
        entityManager.persist(product2);
        entityManager.flush();

        // When
        List<Product> products = productRepository.findAll();

        // Then
        assertEquals(2, products.size());
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Product 1")));
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Product 2")));
    }

    @Test
    public void testUpdateProduct() {
        // Given
        Product product = new Product("Initial Name", 19.99);
        entityManager.persist(product);
        entityManager.flush();

        // When
        product.setName("Updated Name");
        product.setPrice(29.99);
        Product updatedProduct = productRepository.save(product);

        // Then
        assertEquals("Updated Name", updatedProduct.getName());
        assertEquals(29.99, updatedProduct.getPrice());
    }

    @Test
    public void testDeleteProduct() {
        // Given
        Product product = new Product("To Delete", 9.99);
        entityManager.persist(product);
        entityManager.flush();

        // When
        productRepository.deleteById(product.getId());
        Optional<Product> deletedProduct = productRepository.findById(product.getId());

        // Then
        assertFalse(deletedProduct.isPresent());
    }

    @Test
    public void testFindByIdNotFound() {
        // When
        Optional<Product> foundProduct = productRepository.findById(999L);

        // Then
        assertFalse(foundProduct.isPresent());
    }
}
