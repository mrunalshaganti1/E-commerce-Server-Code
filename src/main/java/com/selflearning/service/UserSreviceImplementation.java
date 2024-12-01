package com.selflearning.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.selflearning.config.JwtProvider;
import com.selflearning.exception.UserException;
import com.selflearning.model.User;
import com.selflearning.repository.UserRepository;

@Service
public class UserSreviceImplementation implements UserService{

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtProvider jwtProvider;
	
	@Override
	public User findUserById(Long userId) throws UserException {
		// TODO Auto-generated method stub
		Optional<User> user = userRepository.findById(userId);
		
		if(user.isPresent()) {
			return user.get();
		}
		throw new UserException("User Not Found with Id: "+userId);
	}

	@Override
	public User findUserProfileByJwt(String jwt) throws UserException {
		// TODO Auto-generated method stub
		String email = jwtProvider.getEmailFromToken(jwt);
		
		User user = userRepository.findByEmail(email);
		
		if(user == null) {
			throw new UserException("User Not Found with email: "+email);
		}
		return user;
	}

}
