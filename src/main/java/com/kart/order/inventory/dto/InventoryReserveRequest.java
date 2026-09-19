package com.kart.order.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventoryReserveRequest(
        @NotNull(message = "Reserve quantity can not be null")
        @Min(value = 0, message = "Reserve quantity can not be negative")
        Integer quantity
) {
}
