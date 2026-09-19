package com.kart.order.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record InventoryCreateRequest(

        @NotNull(message = "Product id can not be null")
        UUID productId,

        @NotNull(message = "Available quantity can not be null")
        @Min(value = 0, message = "Available quantity can not be negative")
        Integer availableQuantity
) {
}
