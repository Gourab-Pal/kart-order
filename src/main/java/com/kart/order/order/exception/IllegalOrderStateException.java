package com.kart.order.order.exception;

import java.util.UUID;

public class IllegalOrderStateException extends RuntimeException {
    public IllegalOrderStateException(UUID orderId, String expectedState,  String actualState) {
        super("Order state expected: " + expectedState + ", found actual: " + actualState);
    }
}
