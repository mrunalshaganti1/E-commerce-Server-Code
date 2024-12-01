package com.selflearning.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.selflearning.exception.ProductException;
import com.selflearning.model.Cart;
import com.selflearning.model.CartItem;
import com.selflearning.model.Product;
import com.selflearning.model.User;
import com.selflearning.repository.CartRepository;
import com.selflearning.request.AddItemRequest;

@Service
public class CartServiceImplementation implements CartService{
	
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private CartItemService cartitemService;
	@Autowired
	private ProductService productService;
	

	@Override
	public Cart createCart(User user) {
		// TODO Auto-generated method stub 
		Cart cart = new Cart();
		cart.setUser(user);
		
		return cartRepository.save(cart);
	}

	@Override
	public String addCartItem(Long userId, AddItemRequest request) throws ProductException {
	    // Fetch the cart for the user
	    Cart cart = cartRepository.findByUserId(userId);
	    Product product = productService.findProductById(request.getProductId());

	    // Check if the CartItem already exists in the cart
	    CartItem isPresent = cartitemService.isCartItemExisting(cart, product, request.getSize(), userId);

	    if (isPresent == null) {
	        // Create a new CartItem if it doesn't exist
	        CartItem cartItem = new CartItem();
	        cartItem.setProduct(product);
	        cartItem.setCart(cart);
	        cartItem.setQuantity(request.getQuantity());
	        cartItem.setUserId(userId);

	        // Calculate the price and discounted price for the CartItem
	        int price = request.getQuantity() * product.getPrice();
	        int discountedPrice = request.getQuantity() * product.getDiscountedPrice();
	        cartItem.setPrice(price);
	        cartItem.setDiscountedPrice(discountedPrice);
	        cartItem.setSize(request.getSize());

	        // Save the new CartItem
	        CartItem createdCartItem = cartitemService.createCartItem(cartItem);

	        // Add the new CartItem to the cart
	        cart.getCartItems().add(createdCartItem);
	    } else {
	        // Update the quantity and price of the existing CartItem
	        isPresent.setQuantity(isPresent.getQuantity() + request.getQuantity());
	        isPresent.setPrice(isPresent.getQuantity() * product.getPrice());
	        isPresent.setDiscountedPrice(isPresent.getQuantity() * product.getDiscountedPrice());
	        cartitemService.createCartItem(isPresent); // Save updated CartItem
	    }

	    // Recalculate the cart totals
	    recalculateCartTotals(cart);

	    return "Item added to cart.";
	}

	// Helper method to recalculate cart totals
	private void recalculateCartTotals(Cart cart) {
	    int totalPrice = 0;
	    int totalDiscountedPrice = 0;
	    int totalItems = 0;

	    for (CartItem cartItem : cart.getCartItems()) {
	        totalPrice += cartItem.getPrice();
	        totalDiscountedPrice += cartItem.getDiscountedPrice();
	        totalItems += cartItem.getQuantity();
	    }

	    cart.setTotalPrice(totalPrice);
	    cart.setTotalDiscountedPrice(totalDiscountedPrice);
	    cart.setTotalItems(totalItems);
	    cart.setDiscount(totalPrice - totalDiscountedPrice);

	    // Save the updated cart
	    cartRepository.save(cart);
	}


	@Override
	public Cart findUserCart(Long userId) {
		// TODO Auto-generated method stub
		Cart cart = cartRepository.findByUserId(userId);
		
		int totalPrice = 0;
		int totalDiscountedPrice = 0;
		int totalItem = 0;
		
		for(CartItem cartItem: cart.getCartItems()) {
			totalPrice += cartItem.getPrice();
			totalDiscountedPrice += cartItem.getDiscountedPrice();
			totalItem += cartItem.getQuantity();
		}
		
		cart.setTotalDiscountedPrice(totalDiscountedPrice);
		cart.setTotalItems(totalItem);
		cart.setTotalPrice(totalPrice);
		cart.setDiscount(totalPrice - totalDiscountedPrice);
		return cartRepository.save(cart);
	}

}
