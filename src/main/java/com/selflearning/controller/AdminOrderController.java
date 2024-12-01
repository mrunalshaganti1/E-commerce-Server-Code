package com.selflearning.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.selflearning.exception.OrderException;
import com.selflearning.model.Order;
import com.selflearning.service.OrderService;

@RestController
@RequestMapping("/admin/orders")
public class AdminOrderController {

	@Autowired
	private OrderService orderService;
	
	@GetMapping("/")
	public ResponseEntity<List<Order>> getAllOrders(){
		List<Order> allOrders = orderService.getAllOrders();
		
		return new ResponseEntity<List<Order>>(allOrders,HttpStatus.OK);
	}
	
	@GetMapping("/{orderId}/confirmed")
	public ResponseEntity<Order> confirmedOrderHandler(@RequestHeader("Authorization") String jwt, @PathVariable Long orderId) throws OrderException{
		
		Order order = orderService.confirmedOrder(orderId);

		return new ResponseEntity<Order>(order,HttpStatus.OK);
	}
	
	@GetMapping("/{orderId}/shippedOrder")
	public ResponseEntity<Order> shippedOrderHandler(@RequestHeader("Authorization") String jwt, @PathVariable Long orderId) throws OrderException{
		
		Order order = orderService.shippedOrder(orderId);

		return new ResponseEntity<Order>(order,HttpStatus.OK);
	}
	
	@GetMapping("/{orderId}/deliveredOrder")
	public ResponseEntity<Order> deliveredOrderHandler(@RequestHeader("Authorization") String jwt, @PathVariable Long orderId) throws OrderException{
		
		Order order = orderService.deliveredOrder(orderId);

		return new ResponseEntity<Order>(order,HttpStatus.OK);
	}
	
	@GetMapping("/{orderId}/cancledOrder")
	public ResponseEntity<Order> cancledOrderHandler(@RequestHeader("Authorization") String jwt, @PathVariable Long orderId) throws OrderException{
		
		Order order = orderService.cancledOrder(orderId);

		return new ResponseEntity<Order>(order,HttpStatus.OK);
	}
	
	@DeleteMapping("/{orderId}/delete")
	public String deleteOrderHandler(@RequestHeader("Authorization") String jwt, @PathVariable Long orderId) throws OrderException{
		
		orderService.deleteOrder(orderId);
		
		return "Order Deleted Successfully!";
	}
}
