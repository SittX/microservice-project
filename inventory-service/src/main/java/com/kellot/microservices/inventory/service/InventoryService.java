package com.kellot.microservices.inventory.service;

import com.kellot.microservices.inventory.dto.InventoryRequest;
import com.kellot.microservices.inventory.dto.InventoryResponse;
import com.kellot.microservices.inventory.model.Inventory;
import com.kellot.microservices.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public List<InventoryResponse> getAllInventoryItems(){
        return inventoryRepository.findAll().stream()
                .map(InventoryResponse::from)
                .toList();
    }

    public InventoryResponse findItemBySkuCode(String skuCode){
        log.info("Finding sku code : {}", skuCode);
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode).orElseThrow(() -> new RuntimeException("Error finding item with sku code : " + skuCode));
        return InventoryResponse.from(inventory);
    }

    public InventoryResponse addNewItem(InventoryRequest inventoryRequest){
        Inventory inventory = Inventory.builder()
                .skuCode(inventoryRequest.skuCode())
                .quantity(inventoryRequest.quantity())
                .build();

        Inventory savedInventory = inventoryRepository.save(inventory);

        return InventoryResponse.from(savedInventory);
    }

    public InventoryResponse updateItem(InventoryRequest inventoryRequest) {
      Inventory inventory = inventoryRepository.findBySkuCode(inventoryRequest.skuCode())
              .orElseThrow(() -> new RuntimeException("Error finding sku code : " + inventoryRequest.skuCode()));

      inventory.setQuantity(inventoryRequest.quantity());
      Inventory updatedInventory = inventoryRepository.save(inventory);
      return InventoryResponse.from(updatedInventory);
    }
}
