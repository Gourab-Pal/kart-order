package com.kart.order.inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record InventoryReserveRequest(
        @NotNull(message = "Reserve quantity can not be null")
        @Positive(message = "Reserve quantity must be greater than zero")
        Integer quantity
) {
}
