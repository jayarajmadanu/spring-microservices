package com.js.cart_service.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.js.cart_service.dto.AddToCartRequest;
import com.js.cart_service.dto.AddToCartResponse;
import com.js.cart_service.dto.CartResponse;
import com.js.cart_service.model.Cart;
import com.js.cart_service.repository.CartRepository;

import reactor.core.publisher.Mono;

@Service
public class CartService {

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	class CartAndProduct{
		Long cartId;
		Integer quantity;
		
		public CartAndProduct(Long cId, Integer quantity) {
			this.cartId = cId;
			this.quantity = quantity;
		}
		
		@Override
		public String toString() {
			return this.cartId + " " + this.quantity;
		}
	}
	public CartResponse[] getCartItemsByCustId(String custId) {
		List<Cart> carts = cartRepository.findAllByCustomerId(custId);
		List<String> productIds = carts.stream().map(cart -> cart.getProductId()).toList();
		
		CartResponse[] res = webClientBuilder.build()
			.post()
			.uri("http://product-service/api/product/ids")
			.body(Mono.just(productIds), List.class)
			.retrieve()
			.bodyToMono(CartResponse[].class)
			.block();
		
		HashMap<String, CartAndProduct> hm = new HashMap<>();
		carts.stream().forEach(cart -> hm.put(cart.getProductId(), new CartAndProduct(cart.getId(), cart.getQuantity())));
		Arrays.stream(res).forEach(r -> {
			if(hm.containsKey(r.getProductId())) {
				r.setProductQuantity(hm.get(r.getProductId()).quantity);
				r.setCartId(hm.get(r.getProductId()).cartId);
			}
		});
		return res;
	}

	public AddToCartResponse addToCart(AddToCartRequest addToCartRequest) {
		Cart cart = new Cart();
		cart.setCustomerId(addToCartRequest.getCustomerId());
		cart.setProductId(addToCartRequest.getProductId());
		cart.setQuantity(addToCartRequest.getQuantity());
		AddToCartResponse response =  new AddToCartResponse();
		try {
			Cart res = cartRepository.save(cart);
			response.setMessage("Product added to Cart for customer " + res.getCustomerId());
			response.setResult(true);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setResult(false);
		}
		return response;
	}

	public AddToCartResponse deleteCartById(Long cartId) {
		AddToCartResponse response =  new AddToCartResponse();
		try {
			cartRepository.deleteById(cartId);
			response.setMessage("Product Removed from Cart for customer " + cartId);
			response.setResult(true);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setResult(false);
		}
		
		return response;
	}

	public AddToCartResponse emptyCart(String custId) {
		AddToCartResponse response =  new AddToCartResponse();
		try {
			cartRepository.deleteAllByCustomerId(custId);
			response.setMessage("Emptyed Cart for customer " + custId);
			response.setResult(true);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setResult(false);
		}
		
		return response;
	}
}
