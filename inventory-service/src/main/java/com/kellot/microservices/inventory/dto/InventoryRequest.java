package com.kellot.microservices.inventory.dto;

public record InventoryRequest(String skuCode, int quantity) {
}
