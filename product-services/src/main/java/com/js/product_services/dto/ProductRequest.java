package com.js.product_services.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
	private String productName;
	private String productDescription;
	private BigDecimal productPrice;
	private String productImageUrl;
	private float productOffer;

}
