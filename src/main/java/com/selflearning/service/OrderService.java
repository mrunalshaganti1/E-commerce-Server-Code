package com.selflearning.service;

import java.util.List;

import com.selflearning.exception.OrderException;
import com.selflearning.model.Address;
import com.selflearning.model.Order;
import com.selflearning.model.User;



public interface OrderService {

	public Order createOrder(User user, Address shippingAddress);
	
	public Order findOneById(Long orderId) throws OrderException;
	
	public List<Order> userOrderHistory(Long userId);
	
	public Order placedOrder(Long orderId) throws OrderException;
	
	public Order confirmedOrder(Long orderId) throws OrderException;
	
	public Order shippedOrder(Long orderId) throws OrderException;
	
	public Order deliveredOrder(Long orderId) throws OrderException;
	
	public Order cancledOrder(Long orderId) throws OrderException;
	
	public List<Order> getAllOrders();
	
	public String deleteOrder(Long orderId) throws OrderException;
}
