package com.kart.order.cart.repository;

import com.kart.order.cart.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItemEntity, UUID> {

    Optional<CartItemEntity> findByCartIdAndProductId(UUID cartId, UUID productId);
    List<CartItemEntity> findAllByCartId(UUID cartId);
    void deleteAllByCartId(UUID cartId);
}
