package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepository;
	
	public String createProduct(Product product) {
		productRepository.save(product);
		return "Product created successfully";
	}
	
	public List<Product> getAllProducts(){
		return productRepository.findAll();
	}
	
	public Product getProductById(Long id) {
		return productRepository.findById(id).orElse(null);
	}
	
//	public String updateProduct(Long id, Product product) {
//		if(productRepository.existsById(id)) {
//			productRepository.save(product);
//			return "Product updated successfully";
//		} 
//		return "Product wasn't found";
//}
	
	public String updateProduct(Long id, Product product) {
	    if (productRepository.existsById(id)) {
	        // Retrieve the existing product from the database
	        Product existingProduct = productRepository.findById(id).get();
	        
	        // Update the existing product fields with the new ones
	        existingProduct.setName(product.getName());
	        existingProduct.setPrice(product.getPrice());
	        
	        // Save the updated product
	        productRepository.save(existingProduct);
	        
	        return "Product updated successfully";
	    }
	    return "Product not found";
	}

	
	public String deleteProduct(Long id) {
		if(productRepository.existsById(id)) {
			productRepository.deleteById(id);
			return "Product has been deleted successfully";
		}
		return null;
	}
}
