package com.selflearning.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.selflearning.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	@Query("SELECT p FROM Product p " +
		       "WHERE (:category IS NULL OR p.category.name = :category) " +
		       "AND (:minPrice IS NULL OR :maxPrice IS NULL OR (p.discountedPrice BETWEEN :minPrice AND :maxPrice)) " +
		       "AND (:minDiscount IS NULL OR p.discountPresent >= :minDiscount) " +
		       "ORDER BY " +
		       "CASE WHEN :sort = 'price_low' THEN p.discountedPrice END ASC, " +
		       "CASE WHEN :sort = 'price_high' THEN p.discountedPrice END DESC, " +
		       "p.id ASC")
		List<Product> filterProducts(
		        @Param("category") String category,
		        @Param("minPrice") Integer minPrice,
		        @Param("maxPrice") Integer maxPrice,
		        @Param("minDiscount") Integer minDiscount,
		        @Param("sort") String sort);
	 
}