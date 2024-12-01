package com.selflearning.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.selflearning.exception.OrderException;
import com.selflearning.model.Order;
import com.selflearning.repository.OrderRepository;
import com.selflearning.response.ApiResponse;
import com.selflearning.response.PaymentLinkResponse;
import com.selflearning.service.OrderService;
import com.selflearning.service.UserService;

@RestController
@RequestMapping("/api")
public class PaymentController {
	
	@Value("${razorpay.api.key}")
	String apiKey;
	
	@Value("${razorpay.api.secret}")
	String apiSecret;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@PostMapping("/payments/{orderId}")
	public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable Long orderId,
																 @RequestHeader("Authorization") String jwt) 
																		 		throws OrderException, RazorpayException{
		Order order = orderService.findOneById(orderId);
		
		try {
			RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);
			
			JSONObject paymentLinkRequest = new JSONObject();
			
			paymentLinkRequest.put("amount", order.getTotalPrice() * 100);
			paymentLinkRequest.put("currency", "INR");
			
			JSONObject customer = new JSONObject();
			
			customer.put("Name", order.getUser().getFirstName());
			customer.put("Email", order.getUser().getEmail());
			paymentLinkRequest.put("customer", customer);
			
			JSONObject notifications = new JSONObject();
			notifications.put("sms", true);
			notifications.put("email", true);
			paymentLinkRequest.put("notify", notifications);
			
			paymentLinkRequest.put("callback_url", "http://localhost:3000/payment/"+order.getId());
			paymentLinkRequest.put("callback_method", "get");
			
			PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkRequest);
			
			String paymentLinkId = payment.get("id");
			String paymentLinkUrl = payment.get("short_url");
			
			PaymentLinkResponse response = new PaymentLinkResponse();
			
			response.setPayment_link_id(paymentLinkId);
			response.setPayment_link_url(paymentLinkUrl);
			
			return new ResponseEntity<PaymentLinkResponse>(response, HttpStatus.CREATED);
		} catch (Exception e) {
			// TODO: handle exception
			throw new RazorpayException(e.getMessage());
		}
		
	}
	
	@GetMapping("/payments")
	public ResponseEntity<String> redirect(@RequestParam(name="payment_id") String paymentId, 
												@RequestParam(name= "order_id") Long orderId) throws OrderException, RazorpayException{
		Order order = orderService.findOneById(orderId);
		
		RazorpayClient razorpayClient = new RazorpayClient(apiKey,apiSecret);
		
		try {
			Payment payment = razorpayClient.payments.fetch(paymentId);
			
			if(payment.get("status").equals("captured")) {
				order.getPaymentDetails().setStatus("COMPLETED");
				order.setOrderStatus("PLACED");
				orderRepository.save(order);
			}
			return new ResponseEntity<String>("Your order is Placed", HttpStatus.ACCEPTED);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
