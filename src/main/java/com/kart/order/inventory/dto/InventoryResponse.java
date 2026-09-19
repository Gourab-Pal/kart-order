package com.kart.order.inventory.dto;

import com.kart.order.inventory.entity.InventoryEntity;

import java.time.OffsetDateTime;
import java.util.UUID;

public record InventoryResponse(
        UUID id,
        UUID productId,
        int availableQuantity,
        int reservedQuantity,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static InventoryResponse from(InventoryEntity inventoryEntity) {
        return new InventoryResponse(
                inventoryEntity.getId(),
                inventoryEntity.getProductId(),
                inventoryEntity.getAvailableQuantity(),
                inventoryEntity.getReservedQuantity(),
                inventoryEntity.getCreatedAt(),
                inventoryEntity.getUpdatedAt()
        );
    }
}
