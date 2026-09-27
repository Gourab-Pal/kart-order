package com.kart.order.cart.exception;

import java.util.UUID;

public class IneligibleProductException extends RuntimeException {
    public IneligibleProductException(UUID productId, String message) {
        super("Product " + productId + " is not eligible to be added to cart. Reason: " + message);
    }
}
