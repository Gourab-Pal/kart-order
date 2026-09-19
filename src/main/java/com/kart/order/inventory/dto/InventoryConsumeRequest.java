package com.kart.order.inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record InventoryConsumeRequest(
        @NotNull(message = "Consume quantity can not be null")
        @Positive(message = "Consume quantity must be greater than zero")
        Integer quantity
) {
}
