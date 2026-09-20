package com.kart.order.checkout.exception;

import java.util.UUID;

public class CheckoutException extends RuntimeException {
    public CheckoutException(UUID cartId, String message) {
        super("Checkout failed for cart " + cartId + ", message: " + message);
    }
}
