package com.selflearning.service;

import com.selflearning.exception.CartItemException;
import com.selflearning.exception.UserException;
import com.selflearning.model.Cart;
import com.selflearning.model.CartItem;
import com.selflearning.model.Product;
import com.selflearning.request.AddItemRequest;

public interface CartItemService {

	public CartItem createCartItem(CartItem cartItem);
	
	public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException;
	
	public CartItem isCartItemExisting(Cart cart, Product product, String size, Long userId);
	
	public String removeCartItem(Long userId, Long CartIemId) throws CartItemException, UserException;
	
	public CartItem findCartItemById(Long cartItemId) throws CartItemException;

	public String updateCartItem(Long userId, Long cartItemId, AddItemRequest updateRequest)
			throws CartItemException, UserException;
}
