package com.js.order_service.servive;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.js.order_service.dto.InventoryResponse;
import com.js.order_service.dto.OrderLineItemsDto;
import com.js.order_service.dto.OrderRequest;
import com.js.order_service.event.OrderPlacedEvent;
import com.js.order_service.model.Order;
import com.js.order_service.model.OrderLineItems;
import com.js.order_service.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Autowired
	private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
	
	public String placeOrder(OrderRequest orderRequest) {
		 Order order = new Order();
	        order.setOrderNumber(UUID.randomUUID().toString());

	        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
	                .stream()
	                .map(this::mapToDto)
	                .toList();

	        order.setOrderLineItems(orderLineItems);
	        List<String> skuCodes = order.getOrderLineItems().stream()
	        		.map(OrderLineItems::getSkuCode)
	        		.toList();
	        InventoryResponse[] result = null;
	        try {
	        	result = webClientBuilder.build().get().uri("https://inventory-service/api/inventory", 
	        		uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
	        .retrieve()
	        .bodyToMono(InventoryResponse[].class)
	        .block();
	        } catch(Exception e) {
	        	System.out.println(e.getMessage());
	        }
	        
	        boolean allProductsInStock = Arrays.stream(result).allMatch(res -> res.isInStock());
	        
	        if(allProductsInStock) {
	        	orderRepository.save(order);
	        	CompletableFuture<SendResult<String, OrderPlacedEvent>> future = kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(this, order.getOrderNumber()));
	        	try {
					future.get(10000, TimeUnit.MILLISECONDS);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				} 
	        } else {
	        	throw new IllegalArgumentException("Product is not in stock");
	        }

	       
		return "SUCCESS";
	}
	
	private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
