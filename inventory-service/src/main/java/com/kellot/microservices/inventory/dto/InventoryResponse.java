package com.kellot.microservices.inventory.dto;

import com.kellot.microservices.inventory.model.Inventory;

public record InventoryResponse(long id, String skuCode, int quantity) {
    public static InventoryResponse from(Inventory inventory){
        return new InventoryResponse(inventory.getId(), inventory.getSkuCode(), inventory.getQuantity());
    }
}
