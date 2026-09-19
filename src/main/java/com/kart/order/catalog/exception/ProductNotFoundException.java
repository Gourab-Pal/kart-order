package com.kart.order.catalog.exception;

import java.util.UUID;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(UUID productId) {
        super("Product with id: " + productId + " not found");
    }
}
