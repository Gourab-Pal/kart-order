package com.kart.order.cart.dto;

import com.kart.order.cart.entity.CartEntity;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CartResponse(
        UUID id,
        String status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static CartResponse from(CartEntity cartEntity) {
        return new CartResponse(
                cartEntity.getId(),
                cartEntity.getStatus(),
                cartEntity.getCreatedAt(),
                cartEntity.getUpdatedAt()
        );
    }
}
