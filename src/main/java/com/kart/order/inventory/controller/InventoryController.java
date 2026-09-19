package com.kart.order.inventory.controller;

import com.kart.order.inventory.dto.InventoryCreateRequest;
import com.kart.order.inventory.dto.InventoryResponse;
import com.kart.order.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/create")
    public InventoryResponse createInventory(
            @Valid
            @RequestBody InventoryCreateRequest inventoryCreateRequest
    ) {
       return inventoryService.createInventory(inventoryCreateRequest);
    }
}
