package com.js.cart_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.js.cart_service.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{

	List<Cart> findAllByCustomerId(String id);
	void deleteAllByCustomerId(String id);
}
