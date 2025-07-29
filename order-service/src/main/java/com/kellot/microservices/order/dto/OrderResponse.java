package com.kellot.microservices.order.dto;

import com.kellot.microservices.order.model.Order;

import java.math.BigDecimal;

public record OrderResponse(Long id, String orderNumber, String skuCode, BigDecimal price, Integer quantity) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getSkuCode(),
                order.getPrice(),
                order.getQuantity()
        );
    }
}
