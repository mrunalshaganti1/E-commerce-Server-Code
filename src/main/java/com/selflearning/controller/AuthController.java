package com.selflearning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.selflearning.config.JwtProvider;
import com.selflearning.exception.UserException;
import com.selflearning.model.Cart;
import com.selflearning.model.User;
import com.selflearning.repository.UserRepository;
import com.selflearning.request.LoginRequest;
import com.selflearning.response.AuthResponse;
import com.selflearning.service.CartService;
import com.selflearning.service.CustomUserServiceImplementation;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CustomUserServiceImplementation customUserServiceImplementation;
	
	@Autowired
	private CartService cartService; 
	
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException{
		
		System.out.println("Sign up");
		
		String email = user.getEmail();
		String password = user.getPassword();
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		
		User isEmailExisting = userRepository.findByEmail(email);
		
		if(isEmailExisting != null) {
			throw new UserException("Email is Already used with another Account");
		}
		
		User createUser = new User();
		createUser.setEmail(email);
		createUser.setPassword(passwordEncoder.encode(password));
		createUser.setFirstName(firstName);
		createUser.setLastName(lastName);
		
		User savedUser = userRepository.save(createUser);
		Cart cart = cartService.createCart(savedUser);
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtProvider.generateToken(authentication);
					
		AuthResponse authResponse = new AuthResponse();
		
		authResponse.setJwt(token);
		authResponse.setMessage("Sign Up Success");
			        
		return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
	}

	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest) throws UserException{
		String username = loginRequest.getEmail();
		String password = loginRequest.getPassword();
		
		Authentication authentication = authenticate(username, password);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtProvider.generateToken(authentication);
		
	    AuthResponse authResponse = new AuthResponse();
			
	    authResponse.setJwt(token);
		authResponse.setMessage("SignIn Success");
	        
		
		return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
	}

	private Authentication authenticate(String username, String password) {
		// TODO Auto-generated method stub
		UserDetails userDetails = customUserServiceImplementation.loadUserByUsername(username);
		
		if(userDetails == null) {
			throw new BadCredentialsException("User Name is invalid...");
		}
		
		if(!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Password is Invalid...");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
} 
