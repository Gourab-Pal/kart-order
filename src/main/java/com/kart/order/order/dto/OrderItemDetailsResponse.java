package com.kart.order.order.dto;

import com.kart.order.order.entity.OrderItemEntity;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemDetailsResponse(
        UUID productId,
        String name,
        String sku,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal grossPrice
) {
    public static OrderItemDetailsResponse from(OrderItemEntity orderItemEntity) {
        return new OrderItemDetailsResponse(
                orderItemEntity.getProductId(),
                orderItemEntity.getProductName(),
                orderItemEntity.getSku(),
                orderItemEntity.getQuantity(),
                orderItemEntity.getUnitPrice(),
                orderItemEntity.getLineTotal()
        );
    }
}
