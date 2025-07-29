package com.kellot.microservices.order.service;

import com.kellot.microservices.order.client.InventoryServiceClient;
import com.kellot.microservices.order.dto.InventoryRequest;
import com.kellot.microservices.order.dto.InventoryResponse;
import com.kellot.microservices.order.dto.OrderRequest;
import com.kellot.microservices.order.dto.OrderResponse;
import com.kellot.microservices.order.exception.QuantityInsufficientException;
import com.kellot.microservices.order.model.Order;
import com.kellot.microservices.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryServiceClient inventoryServiceClient;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        InventoryResponse inventoryItem = inventoryServiceClient.getInventoryBySkuCode(orderRequest.skuCode());

        if(inventoryItem == null){
           throw new EntityNotFoundException("Item with sku code " + orderRequest.skuCode() + "has not been found in the inventory service");
        }

        if(inventoryItem.quantity() < orderRequest.quantity()){
            throw new QuantityInsufficientException("Insufficient inventory item for request { Inventory :  " + inventoryItem.quantity() +", Request : " + orderRequest.quantity() + " }" );
        }

        int updatedQuantity = inventoryItem.quantity() - orderRequest.quantity();
        if(updatedQuantity < 0){
            throw new QuantityInsufficientException("Invalid quantity { Inventory :  " + inventoryItem.quantity() +", Request : " + orderRequest.quantity() + " }" );
        }

        try {
            var response = inventoryServiceClient.updateInventory(new InventoryRequest(inventoryItem.skuCode(), updatedQuantity));

            Order order = Order.builder()
                    .orderNumber(orderRequest.orderNumber())
                    .skuCode(orderRequest.skuCode())
                    .price(orderRequest.price())
                    .quantity(orderRequest.quantity())
                    .build();

            orderRepository.save(order);
            log.info("Order placed successfully: {}", order);

            return OrderResponse.from(order);
        }catch(Exception exception){
            log.error("Error creating order : {}", orderRequest.skuCode());
            throw new RuntimeException("Error placing order : " + exception.getMessage());
        }
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        log.info("Retrieved all orders: {}", orders);

        return orders.stream()
                .map(OrderResponse::from)
                .toList();
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        log.info("Retrieved order by id {}: {}", id, order);

        return OrderResponse.from(order);
    }
}
