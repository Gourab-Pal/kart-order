package com.kart.order.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CartItemCreateRequest(
        @NotNull(message = "Product id is required")
        UUID productId,

        @NotNull(message = "Quantity in cart can not be null")
        @Positive(message = "Quantity to add to cart can not be negative")
        Integer quantity
) {
}
