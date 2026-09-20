package com.kart.order.order.repository;

import com.kart.order.order.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, UUID> {
    List<OrderItemEntity> findAllByOrderId(UUID orderId);
}
