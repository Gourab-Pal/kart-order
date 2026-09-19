package com.kart.order.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventoryReleaseRequest(
        @NotNull(message = "Release quantity can not be null")
        @Min(value = 0, message = "Release quantity can not be negative")
        Integer quantity
) {
}
