package com.selflearning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.selflearning.exception.CartItemException;
import com.selflearning.exception.ProductException;
import com.selflearning.exception.UserException;
import com.selflearning.model.Cart;
import com.selflearning.model.User;
import com.selflearning.request.AddItemRequest;
import com.selflearning.service.CartItemService;
import com.selflearning.service.CartService;
import com.selflearning.service.UserService;

@RestController
@RequestMapping("/api/cart")
//@Tag(name = "Cart Management", description = "Find User Cart, add item to cart")
public class CartController {

	@Autowired
	private CartService cartService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartItemService cartItemService;
	
	
	@GetMapping("/")
	public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) throws UserException{
		User user = userService.findUserProfileByJwt(jwt);
		Cart cart = cartService.findUserCart(user.getId());
		
		return new ResponseEntity<Cart>(cart, HttpStatus.OK);
	}
	
	@PostMapping("/addItemToCart")
	public ResponseEntity<String> addItemToCart(@RequestHeader("Authorization") String jwt, @RequestBody AddItemRequest addItemRequest) throws UserException, ProductException{
		
		User user = userService.findUserProfileByJwt(jwt);
		String response = cartService.addCartItem(user.getId(), addItemRequest);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/removeCartItem/{cartItemId}")
	public ResponseEntity<String> deleteItemFromCart(@RequestHeader("Authorization") String jwt, @PathVariable Long cartItemId) throws UserException, ProductException{
		try {
			User user = userService.findUserProfileByJwt(jwt);
			String response = cartItemService.removeCartItem(user.getId(), cartItemId);
			return ResponseEntity.status(200).body(response);
		} catch (CartItemException | UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(400).body("No product with that Id!");
		}
		
	}
	
	@PutMapping("/updateCartItem/{cartItemId}")
	public ResponseEntity<String> updateCartItem(
	        @RequestHeader("Authorization") String jwt,
	        @PathVariable Long cartItemId,
	        @RequestBody AddItemRequest updateRequest
	) throws UserException, ProductException, CartItemException {
	    User user = userService.findUserProfileByJwt(jwt);
	    String response = cartItemService.updateCartItem(user.getId(), cartItemId, updateRequest);
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
