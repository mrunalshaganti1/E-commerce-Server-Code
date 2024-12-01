package com.selflearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.selflearning.model.Cart;
import com.selflearning.model.CartItem;
import com.selflearning.model.Product;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{

	@Query("SELECT ci FROM CartItem ci WHERE ci.cart =:cart AND ci.product =:product AND ci.size =:size AND ci.userId =:userId")
	public CartItem isCartItemExist(@Param("cart") Cart cart, @Param("product") Product product, @Param("size") String size,
									@Param("userId") Long userId);
}
