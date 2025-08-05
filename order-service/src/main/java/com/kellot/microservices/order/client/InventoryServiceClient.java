package com.kellot.microservices.order.client;

import com.kellot.microservices.order.dto.InventoryRequest;
import com.kellot.microservices.order.dto.InventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "inventory-service", url = "${api.gateway.url}/api/inventory")
public interface InventoryServiceClient {
    @GetMapping(value = "/{skuCode}")
    InventoryResponse getInventoryBySkuCode(@PathVariable("skuCode") String skuCode);

    @PutMapping
    InventoryResponse updateInventory(@RequestBody InventoryRequest inventoryRequest);
}
