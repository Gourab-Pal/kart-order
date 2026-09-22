package com.kart.order.order.exception;

import java.util.UUID;

public class IllegalOrderStateException extends RuntimeException {
    public IllegalOrderStateException(UUID orderId, String expectedStatus, String actualStatus) {
        super("Illegal Order State for Order #" + orderId + ". Expected status: " + expectedStatus + ", actual status: " + actualStatus);
    }
}
