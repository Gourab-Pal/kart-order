package com.kart.order.cart.dto;

import com.kart.order.cart.entity.CartItemEntity;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CartItemSummaryResponse(
        UUID productId,
        int quantity,
        OffsetDateTime updatedAt
) {
    public static CartItemSummaryResponse from(CartItemEntity cartItemEntity) {
        return new CartItemSummaryResponse(
                cartItemEntity.getProductId(),
                cartItemEntity.getQuantity(),
                cartItemEntity.getUpdatedAt()
        );
    }
}
