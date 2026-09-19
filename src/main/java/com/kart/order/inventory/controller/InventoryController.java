package com.kart.order.inventory.controller;

import com.kart.order.inventory.dto.*;
import com.kart.order.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    ) {
       return inventoryService.createInventory(inventoryCreateRequest);
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponse getInventory(@PathVariable UUID productId) {
        return inventoryService.findInventory(productId);
    }

    @PostMapping("/{productId}/restock")
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponse restock(@PathVariable UUID productId, @RequestBody @Valid InventoryRestockRequest inventoryRestockRequest) {
        return  inventoryService.restock(productId, inventoryRestockRequest);
    }

    @PostMapping("/{productId}/reserve")
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponse reserve(@PathVariable UUID productId, @RequestBody @Valid InventoryReserveRequest inventoryReserveRequest) {
        return  inventoryService.reserve(productId, inventoryReserveRequest);
    }

    @PostMapping("/{productId}/release")
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponse release(@PathVariable UUID productId, @RequestBody @Valid InventoryReleaseRequest inventoryReleaseRequest) {
        return  inventoryService.release(productId, inventoryReleaseRequest);
    }
}
