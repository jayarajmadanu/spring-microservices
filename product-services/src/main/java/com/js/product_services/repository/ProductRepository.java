package com.js.product_services.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.js.product_services.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

}
