package com.kart.order.order.repository;

import com.kart.order.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    Optional<OrderEntity> findByCartId(UUID cartId);
    boolean existsByCartId(UUID cartId);
}
