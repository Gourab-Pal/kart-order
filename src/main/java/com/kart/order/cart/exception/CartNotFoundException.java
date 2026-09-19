package com.kart.order.cart.exception;

import java.util.UUID;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(UUID cartId) {
        super("Cart with id " + cartId + " not found");
    }
}
