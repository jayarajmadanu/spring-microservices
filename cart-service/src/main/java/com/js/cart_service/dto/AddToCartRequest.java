package com.js.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartRequest {

	private String customerId;
	private String productId;
	private Integer quantity;
}
