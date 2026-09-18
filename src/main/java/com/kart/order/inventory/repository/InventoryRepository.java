package com.kart.order.inventory.repository;

import com.kart.order.inventory.entity.InventoryEntity;
import org.hibernate.internal.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InventoryRepository extends JpaRepository<InventoryEntity, UUID> {
    Optional<InventoryEntity> findByProductId(UUID productId);
    boolean existsByProductId(UUID productId);
}
