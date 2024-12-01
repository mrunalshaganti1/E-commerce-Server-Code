package com.selflearning.service;

import com.selflearning.exception.ProductException;
import com.selflearning.model.Cart;
import com.selflearning.model.User;
import com.selflearning.request.AddItemRequest;

public interface CartService {
	
	public Cart createCart(User user);
	
	public String addCartItem(Long userId, AddItemRequest request) throws ProductException;
	
	public Cart findUserCart(Long userId);
	
}
