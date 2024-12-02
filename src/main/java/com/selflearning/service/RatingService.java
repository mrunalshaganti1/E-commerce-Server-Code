package com.selflearning.service;

import java.util.List;

import com.selflearning.exception.ProductException;
import com.selflearning.model.Rating;
import com.selflearning.model.User;
import com.selflearning.request.RatingRequest;

public interface RatingService {

	public Rating createRating(RatingRequest request, User user) throws ProductException;
	public List<Rating> getProductsRatings(Long productId);
}
