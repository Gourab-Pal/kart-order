package com.kart.order.cart.exception;

import java.util.UUID;

public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException(UUID cartId, UUID productId) {
        super("Could not find product with id: " + productId + " on cart with id: " + cartId);
    }
}
