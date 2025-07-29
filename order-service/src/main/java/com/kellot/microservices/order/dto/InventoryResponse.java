package com.kellot.microservices.order.dto;


public record InventoryResponse(long id, String skuCode, int quantity) {
}
