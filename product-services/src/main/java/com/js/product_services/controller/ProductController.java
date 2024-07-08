package com.js.product_services.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.js.product_services.service.ProductService;
import com.js.product_services.dto.ProductRequest;
import com.js.product_services.dto.ProductResponse;

@RestController
@RequestMapping(value = "/api/product")
@CrossOrigin(origins = "http://localhost:4204")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@PostMapping
	public ResponseEntity<String> createProduct(@RequestBody ProductRequest productRequest) {
		productService.createProduct(productRequest);
		return new ResponseEntity<String>("SUCCESS", HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<ProductResponse>> getAllProducts() {
		List<ProductResponse> res = productService.getAllProducts();
		return new ResponseEntity<List<ProductResponse>>(res, HttpStatus.OK);
	}
	
	@PostMapping("/ids")
	public List<ProductResponse> getAllProductsByProductId(@RequestBody ArrayList<String> productIds) {
		return productService.getAllProductsByProductId(productIds);
	}

}
