package com.selflearning.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.selflearning.model.Order;

@Repository
@EnableJpaRepositories
public interface OrderRepository extends JpaRepository<Order, Long>{
	
	@Query("SELECT o FROM Order o WHERE o.user.Id = :userId AND (o.orderStatus = 'PLACED' OR o.orderStatus = 'CONFIRMED' OR o.orderStatus = 'SHIPPED' OR o.orderStatus = 'DELIVERED' OR o.orderStatus = 'PENDING')")
	public List<Order> getUsersOrders(@Param("userId") Long userId);
}
