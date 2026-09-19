package com.kart.order.cart.exception;

import java.util.UUID;

public class IllegalCartStateException extends RuntimeException {
    public IllegalCartStateException(UUID cartId, String expectedStatus, String actualStatus) {
        super("Illegal cart state found. Expected: " + expectedStatus + ", actual: " + actualStatus);
    }
}
