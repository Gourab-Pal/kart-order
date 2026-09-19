package com.kart.order.inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record InventoryRestockRequest(
        @NotNull(message = "Restock quantity can not be null")
        @Positive(message = "Restock quantity must be greater than zero")
        Integer quantity
) {
}
