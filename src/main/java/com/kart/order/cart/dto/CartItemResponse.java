package com.kart.order.cart.dto;

import com.kart.order.cart.entity.CartItemEntity;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CartItemResponse(
        UUID id,
        UUID cartId,
        UUID productId,
        int quantity,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static CartItemResponse from(CartItemEntity cartItemEntity) {
        return new  CartItemResponse(
                cartItemEntity.getId(),
                cartItemEntity.getCart().getId(),
                cartItemEntity.getProductId(),
                cartItemEntity.getQuantity(),
                cartItemEntity.getCreatedAt(),
                cartItemEntity.getUpdatedAt()
        );
    }
}
