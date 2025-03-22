package com.example.demo.Controller;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;


import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testGetAllProductsController() throws Exception {
        when(productService.getAllProducts()).thenReturn(productList);

        mockMvc.perform(get("/api/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void testGetProductByIdController() throws Exception {
        when(productService.getProductById(1L)).thenReturn(product1);

        mockMvc.perform(get("/api/products/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void testCreateProductController() throws Exception {
        Product newProduct = new Product("Tablet", 499.99);
        String responseMessage = "Product created successfully";

        when(productService.createProduct(any(Product.class))).thenReturn(responseMessage);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(responseMessage));
    }

    @Test
    void testUpdateProductController() throws Exception {
        Product updatedProduct = new Product("Updated Laptop", 1499.99);
        String responseMessage = "Product updated successfully";

        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(responseMessage);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(responseMessage));
    }

    @Test
    void testDeleteProductController() throws Exception {
        String responseMessage = "Product has been deleted successfully";
        when(productService.deleteProduct(1L)).thenReturn(responseMessage);

        mockMvc.perform(delete("/api/products/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(responseMessage));
    }
}