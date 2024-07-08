package com.js.product_services.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.js.product_services.dto.ProductRequest;
import com.js.product_services.dto.ProductResponse;
import com.js.product_services.model.Product;
import com.js.product_services.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	public void createProduct(ProductRequest productRequest) {
		Product product =  Product.builder()
				.name(productRequest.getProductName())
				.price(productRequest.getProductPrice())
				.description(productRequest.getProductDescription())
				.offer(productRequest.getProductOffer())
				.imageUrl(productRequest.getProductImageUrl())
				.build();
		
		productRepository.save(product);
		log.info("Product {} is saved", product.getId());
	}

	public List<ProductResponse> getAllProducts() {
		List<Product> products = productRepository.findAll();
		return products.stream().map(this::mapToProductResponse).toList();
	}
	
	private ProductResponse mapToProductResponse(Product product) {
		return ProductResponse.builder()
				.productId(product.getId())
				.productName(product.getName())
				.productDescription(product.getDescription())
				.productPrice(product.getPrice())
				.productOffer(product.getOffer())
				.productImageUrl(product.getImageUrl())
				.build();
	}

	public List<ProductResponse> getAllProductsByProductId(List<String> productIds) {
		List<Product> products = productRepository.findAllById(productIds);
		return products.stream().map(this::mapToProductResponse).toList();
	}
}
