package com.kart.order.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemQuantityUpdateRequest(

        @NotNull(message = "Quantity in cart can not be null")
        @Positive(message = "Quantity to add to cart must be positive")
        Integer quantity
) {
}
