package com.js.cart_service.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {

	private String productId;
	private Long cartId;
	private String productName;
	private String productDescription;
	private BigDecimal productPrice;
	private String productImageUrl;
	private float productOffer;
	private int productQuantity;
}
