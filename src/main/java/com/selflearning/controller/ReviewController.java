package com.selflearning.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.selflearning.exception.ProductException;
import com.selflearning.exception.UserException;
import com.selflearning.model.Review;
import com.selflearning.model.User;
import com.selflearning.request.ReviewRequest;
import com.selflearning.service.ReviewService;
import com.selflearning.service.UserService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private UserService userService;

	@PostMapping("/createreview")
	public ResponseEntity<Review> createReview(@RequestBody ReviewRequest request,
											   @RequestHeader String jwt) throws UserException, ProductException{
		User user = userService.findUserProfileByJwt(jwt);
		
		Review review = reviewService.createReview(request, user);
		
		return new ResponseEntity<Review>(review, HttpStatus.CREATED);
	}
	
	public ResponseEntity<List<Review>> getProductsReview(@PathVariable Long productId) throws UserException, ProductException{
		
		List<Review> reviews = reviewService.getAllReviews(productId);
		
		return new ResponseEntity<>(reviews, HttpStatus.OK);
	}
	
}
