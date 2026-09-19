package com.kart.order.inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record InventoryReleaseRequest(
        @NotNull(message = "Release quantity can not be null")
        @Positive(message = "Release quantity must be greater than zero")
        Integer quantity
) {
}
