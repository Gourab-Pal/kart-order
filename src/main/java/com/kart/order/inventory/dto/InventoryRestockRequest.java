package com.kart.order.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventoryRestockRequest(
        @NotNull(message = "Restock quantity can not be null")
        @Min(value = 0, message = "Restock quantity can not be negative")
        Integer quantity
) {
}
