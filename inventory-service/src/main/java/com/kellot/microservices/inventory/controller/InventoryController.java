package com.kellot.microservices.inventory.controller;

import com.kellot.microservices.inventory.dto.InventoryRequest;
import com.kellot.microservices.inventory.dto.InventoryResponse;
import com.kellot.microservices.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> getAllInventoryItems(){
        return inventoryService.getAllInventoryItems();
    }

    @GetMapping("/{skuCode}")
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponse findBySkuCode(@PathVariable("skuCode") String skuCode){
        return inventoryService.findItemBySkuCode(skuCode);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponse addNewItem(@RequestBody InventoryRequest inventoryRequest){
        return inventoryService.addNewItem(inventoryRequest);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public InventoryResponse updateItem(@RequestBody InventoryRequest inventoryRequest){
        return inventoryService.updateItem(inventoryRequest);
    }

}
