package com.kellot.microservices.order.dto;

public record InventoryRequest(String skuCode, int quantity) {
}
