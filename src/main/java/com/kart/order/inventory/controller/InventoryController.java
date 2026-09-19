package com.kart.order.inventory.controller;

import com.kart.order.catalog.exception.ProductNotFoundException;
import com.kart.order.inventory.dto.InventoryCreateRequest;
import com.kart.order.inventory.dto.InventoryResponse;
import com.kart.order.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponse createInventory(
            @Valid
            @RequestBody InventoryCreateRequest inventoryCreateRequest
    ) throws ProductNotFoundException {
       return inventoryService.createInventory(inventoryCreateRequest);
    }
}
