package com.js.cart_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.js.cart_service.dto.AddToCartRequest;
import com.js.cart_service.dto.AddToCartResponse;
import com.js.cart_service.dto.CartResponse;
import com.js.cart_service.service.CartService;

import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:4204")
public class CartController {

	@Autowired
	private CartService cartService;
	
	@GetMapping("/GetCartProductsByCustomerId")
	public CartResponse[] getCartItemsByCustId(@RequestParam String custId) {
		return cartService.getCartItemsByCustId(custId);
	}
	
	@PostMapping("/AddToCart")
	public AddToCartResponse addToCart(@RequestBody AddToCartRequest addToCartRequest) {
		return cartService.addToCart(addToCartRequest);
	}
	
	@DeleteMapping("/RemoveFromCart")
	public AddToCartResponse deleteCartById(@RequestParam Long cartId) {
		return cartService.deleteCartById(cartId);
	}
	
	@DeleteMapping("/EmptyCart")
	@Transactional
	public AddToCartResponse emptyCart(@RequestParam String custId) {
		return cartService.emptyCart(custId);
	}
}
