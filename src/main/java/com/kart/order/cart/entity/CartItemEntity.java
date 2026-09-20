package com.kart.order.cart.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "cart_items",
        schema = "kart_order",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_cart_product",
                        columnNames = {"cart_id", "product_id"}
                )
        }
)
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEntity cart;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected CartItemEntity() {
    }

    public CartItemEntity(
            CartEntity cart,
            UUID productId,
            int quantity
    ) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Cart item quantity must be greater than zero"
            );
        }

        this.cart = cart;
        this.productId = productId;
        this.quantity = quantity;
    }

    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public CartEntity getCart() {
        return cart;
    }

    public UUID getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void increaseQuantity(int quantityToAdd) {
        this.quantity = this.quantity + quantityToAdd;
        this.updatedAt = OffsetDateTime.now();
    }

    public void updateItemQuantity(int quantity) {
        this.quantity = this.quantity - quantity;
        this.updatedAt = OffsetDateTime.now();
    }
}