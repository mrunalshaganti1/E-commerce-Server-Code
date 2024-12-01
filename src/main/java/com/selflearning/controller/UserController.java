package com.selflearning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.selflearning.exception.UserException;
import com.selflearning.model.User;
import com.selflearning.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/profile")
	public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt){
		try {
			if(jwt.startsWith("Bearer ")) {
				jwt = jwt.substring(7);
			}
			
			User user = userService.findUserProfileByJwt(jwt);
			return ResponseEntity.ok(user);
		}catch(UserException e) {
			return ResponseEntity.status(404).body(null);
		}
	}
}
