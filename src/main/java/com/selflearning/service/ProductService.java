package com.selflearning.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.selflearning.exception.ProductException;
import com.selflearning.model.Product;
import com.selflearning.request.CreateProductRequest;

public interface ProductService {
	
	public Product createProduct(CreateProductRequest request);
	
	public String deleteProduct(Long productId) throws ProductException;
	
	public Product updateProduct(Long productId, Product product) throws ProductException;
	
	public Product findProductById(Long Id) throws ProductException;
	
	public List<Product> findProductByCategory(String category);
	
	public Page<Product> getAllProducts(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, 
								String sort, String stock, Integer pageNumber, Integer pageSize);

	//public List<Product> searchProduct(String q);
}
