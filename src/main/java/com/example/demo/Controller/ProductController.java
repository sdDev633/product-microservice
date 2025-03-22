package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.ProductService;
import com.example.demo.entity.Product;

@RestController
@RequestMapping("api/products")
public class ProductController {
	@Autowired
	private ProductService productService;
	
	
	@GetMapping
	public ResponseEntity<List<Product>> getAllProductsController(){
		return ResponseEntity.ok(productService.getAllProducts());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Product> getProductByIdController(@PathVariable Long id){
		return ResponseEntity.ok(productService.getProductById(id));
	}
	
	@PostMapping
	public ResponseEntity<String> createProductController(@RequestBody Product product){
		return ResponseEntity.ok(productService.createProduct(product));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<String> updateProductController(@PathVariable Long id, @RequestBody Product product){
		return ResponseEntity.ok(productService.updateProduct(id, product));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProductController(@PathVariable Long id){
		return ResponseEntity.ok(productService.deleteProduct(id));
	}
	
		
}