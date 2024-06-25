package com.js.inventory_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.js.inventory_service.dto.InventoryResponse;
import com.js.inventory_service.model.Inventory;
import com.js.inventory_service.repository.InventoryRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InventoryService {

	@Autowired
	private InventoryRepository inventoryRepository;
	
	public List<InventoryResponse> isInStock(List<String> skuCode){
		
		log.info("Checking Inventory");
		return inventoryRepository.findBySkuCodeIn(skuCode).stream()
				.map( inventory -> 	InventoryResponse.builder()
					.skuCode(inventory.getSkuCode())
					.isInStock(inventory.getQuantity() > 0)
					.build()
				).toList();
	}
}
