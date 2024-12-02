package com.selflearning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.selflearning.exception.ProductException;
import com.selflearning.model.Product;
import com.selflearning.model.User;
import com.selflearning.request.CreateProductRequest;
import com.selflearning.service.ProductService;
import com.selflearning.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/createproduct")
	public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest createProductRequest,
												 @RequestHeader("Authorization") String jwt){
		try {
			if(jwt.startsWith("Bearer ")) {
				jwt = jwt.substring(7);
			}
			
			User user = userService.findUserProfileByJwt(jwt);
			if(user == null) {
				return ResponseEntity.status(403).body(null);
			}
			
			Product createdProduct = productService.createProduct(createProductRequest);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}	
	}
	
	@GetMapping("/id/{productId}")
	public ResponseEntity<String> deleteProduct(@RequestHeader("Authorization") String jwt, @PathVariable Long productId){
		try {
			if(jwt.startsWith("Bearer ")) {
				jwt.substring(7);
			}
			
			User user = userService.findUserProfileByJwt(jwt);
			if(user == null) {
				return ResponseEntity.status(403).body(null);
			}
			String deleteSuccessMessage = productService.deleteProduct(productId);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(deleteSuccessMessage); 
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("No product with that Id");
		}
	}
	
	@PutMapping("/{productId}/update")
	public ResponseEntity<Product> updateProduct(@RequestBody Product requestProduct, @PathVariable Long productId) throws ProductException{
		
		Product product = productService.updateProduct(productId, requestProduct);
		return new ResponseEntity<Product>(product, HttpStatus.CREATED);
	}
	
	@PostMapping("/createmultipleproducts")
	public ResponseEntity<String> createMultipleProduct(@RequestBody CreateProductRequest[] requestProducts){
		for(CreateProductRequest product: requestProducts){
			productService.createProduct(product);
		}
		
		return new ResponseEntity<String>("Products Successfully Added!!", HttpStatus.CREATED);
	}
}
