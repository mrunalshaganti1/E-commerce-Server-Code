package com.selflearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.selflearning.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	public User findByEmail(String email);
}
