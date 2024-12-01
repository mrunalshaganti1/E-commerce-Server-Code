package com.selflearning.service;

import java.util.List;

import com.selflearning.exception.ProductException;
import com.selflearning.model.Review;
import com.selflearning.model.User;
import com.selflearning.request.ReviewRequest;

public interface ReviewService {

	public Review createReview(ReviewRequest request, User user) throws ProductException;
	
	public List<Review> getAllReviews(Long productId);
}
