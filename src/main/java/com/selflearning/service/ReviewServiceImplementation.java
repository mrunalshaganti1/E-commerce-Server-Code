package com.selflearning.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.selflearning.exception.ProductException;
import com.selflearning.model.Product;
import com.selflearning.model.Review;
import com.selflearning.model.User;
import com.selflearning.repository.ReviewRepository;
import com.selflearning.request.ReviewRequest;

@Service
public class ReviewServiceImplementation implements ReviewService{
	
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private ProductService productService;

	@Override
	public Review createReview(ReviewRequest request, User user) throws ProductException {
		// TODO Auto-generated method stub
		Product product = productService.findProductById(request.getProductId());
		
		Review review = new Review();
		review.setUser(user);
		review.setProduct(product);
		review.setReview(request.getReview());
		review.setCreatedAt(LocalDateTime.now());
		
		return reviewRepository.save(review);
	}

	@Override
	public List<Review> getAllReviews(Long productId) {
		// TODO Auto-generated method stub
		return reviewRepository.getAllProductsReview(productId);
	}

}
