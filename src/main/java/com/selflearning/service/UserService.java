package com.selflearning.service;

import com.selflearning.exception.UserException;
import com.selflearning.model.User;

public interface UserService {
	
	public User findUserById(Long userId) throws UserException;
	
	public User findUserProfileByJwt(String jwt) throws UserException;
	
	
}
