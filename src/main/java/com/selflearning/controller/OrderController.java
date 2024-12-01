package com.selflearning.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.selflearning.exception.OrderException;
import com.selflearning.exception.UserException;
import com.selflearning.model.Address;
import com.selflearning.model.Order;
import com.selflearning.model.User;
import com.selflearning.service.OrderService;
import com.selflearning.service.UserService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/createOrder")
	public ResponseEntity<Order> createOrder(@RequestBody Address shippingAddress, @RequestHeader("Authorization") String jwt) throws UserException, OrderException{
		
			User user = userService.findUserProfileByJwt(jwt);
			
			Order order = orderService.createOrder(user, shippingAddress);
			
			System.out.println("Order: " + order);
			
			return new ResponseEntity<Order>(order, HttpStatus.CREATED);
		
	}
	
	@GetMapping("/userHistory")
	public ResponseEntity<List<Order>> usersOrderHistory(@RequestHeader("Authorization") String jwt) throws UserException{
		
			User user = userService.findUserProfileByJwt(jwt);
			
			List<Order> orderHistory = orderService.userOrderHistory(user.getId());
			
			return new ResponseEntity<List<Order>>(orderHistory, HttpStatus.OK);
		
	}
	
	@GetMapping("/{orderId}")
	public ResponseEntity<Order> findOrderById(@RequestHeader("Authorization") String jwt, @PathVariable Long orderId) throws UserException, OrderException{
		
			User user = userService.findUserProfileByJwt(jwt);
			
			Order order = orderService.findOneById(orderId);
			
			return new ResponseEntity<Order>(order, HttpStatus.OK);
	}
}
