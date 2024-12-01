package com.selflearning.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.selflearning.exception.OrderException;
import com.selflearning.model.Address;
import com.selflearning.model.Cart;
import com.selflearning.model.CartItem;
import com.selflearning.model.Order;
import com.selflearning.model.OrderItem;
import com.selflearning.model.User;
import com.selflearning.repository.AddressRepository;
import com.selflearning.repository.CartRepository;
import com.selflearning.repository.OrderItemRepository;
import com.selflearning.repository.OrderRepository;
import com.selflearning.repository.UserRepository;

@Service
public class OrderServiceImplementation implements OrderService{
	
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private CartService cartService;
	@Autowired
	private ProductService productService;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private OrderItemRepository orderItemRepository;
	@Autowired
	private OrderRepository orderRepository;

	@Override
	public Order createOrder(User user, Address shippingAddress) {
		// TODO Auto-generated method stub
		shippingAddress.setUser(user);
		Address address = addressRepository.save(shippingAddress);
		user.getAddress().add(address);
		userRepository.save(user);
		
		Cart cart = cartService.findUserCart(user.getId());
		List<OrderItem> orderItems = new ArrayList<>();
		
		for(CartItem item: cart.getCartItems()) {
			OrderItem orderItem = new OrderItem();
			
			orderItem.setPrice(item.getPrice());
			orderItem.setProduct(item.getProduct());
			orderItem.setQuantity(item.getQuantity());
			orderItem.setSize(item.getSize());
			orderItem.setUserId(item.getUserId());
			orderItem.setDiscountedPrice(item.getDiscountedPrice());
			
			OrderItem createdOrderItem = orderItemRepository.save(orderItem);
			
			orderItems.add(createdOrderItem);
		}
		
		Order createdOrder = new Order();
		createdOrder.setUser(user);
		createdOrder.setOrderItems(orderItems);
		createdOrder.setTotalPrice(cart.getTotalPrice());
		createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
		createdOrder.setDiscount(cart.getDiscount());
		createdOrder.setTotalItems(cart.getTotalItems());
		
		createdOrder.setShippingAddress(shippingAddress);
		createdOrder.setOrderDate(LocalDateTime.now());
		createdOrder.setOrderStatus("PENDING");
		createdOrder.getPaymentDetails().setStatus("PENDING");
		createdOrder.setCreatedAt(LocalDateTime.now());
		
		Order savedOrder = orderRepository.save(createdOrder);
		
		for(OrderItem item: orderItems) {
			item.setOrder(savedOrder);
			orderItemRepository.save(item);
		}
		
		return savedOrder;
	}

	@Override
	public Order findOneById(Long orderId) throws OrderException {
		// TODO Auto-generated method stub
		Optional<Order> opt = orderRepository.findById(orderId);
		
		if(opt.isPresent()) {
			return opt.get();
		}
		
		throw new OrderException("Order does not exit with Id: "+orderId);
	}

	@Override
	public List<Order> userOrderHistory(Long userId) {
		// TODO Auto-generated method stub
		List<Order> orderHistory = orderRepository.getUsersOrders(userId);
		return orderHistory;
	}

	@Override
	public Order placedOrder(Long orderId) throws OrderException {
		// TODO Auto-generated method stub
		Order order = findOneById(orderId);
		order.setOrderStatus("PLACED");
		order.getPaymentDetails().setStatus("COMPLETED");
		return order;
	}

	@Override
	public Order confirmedOrder(Long orderId) throws OrderException {
		// TODO Auto-generated method stub
		Order order = findOneById(orderId);
		order.setOrderStatus("CONFIRMED");
		return orderRepository.save(order);
	}

	@Override
	public Order shippedOrder(Long orderId) throws OrderException {
		// TODO Auto-generated method stub
		Order order = findOneById(orderId);
		order.setOrderStatus("SHIPPED");
		return orderRepository.save(order);
	}

	@Override
	public Order deliveredOrder(Long orderId) throws OrderException {
		// TODO Auto-generated method stub
		Order order = findOneById(orderId);
		order.setOrderStatus("DELIVERED");
		return orderRepository.save(order);
	}

	@Override
	public Order cancledOrder(Long orderId) throws OrderException {
		// TODO Auto-generated method stub
		Order order = findOneById(orderId);
		order.setOrderStatus("CANCELLED");
		return orderRepository.save(order);
	}

	@Override
	public List<Order> getAllOrders() {
		// TODO Auto-generated method stub
		return orderRepository.findAll();
	}

	@Override
	public String deleteOrder(Long orderId) throws OrderException {
		// TODO Auto-generated method stub
		Order order = findOneById(orderId);
		
		orderItemRepository.deleteById(order.getId());
		
		return "Order Deleted Successfully";
	}

}
