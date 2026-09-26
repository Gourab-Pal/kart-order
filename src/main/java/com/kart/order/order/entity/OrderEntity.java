package com.kart.order.order.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders", schema = "kart_order")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "cart_id", nullable = false, unique = true)
    private UUID cartId;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(
            name = "total_amount",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal totalAmount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected OrderEntity() {
    }

    public OrderEntity(
            UUID cartId
    ) {
        if (cartId == null) {
            throw new IllegalArgumentException("Cart id cannot be null");
        }

        this.cartId = cartId;
        this.status = "PENDING";
        this.totalAmount = BigDecimal.ZERO;
        this.currency = "INR";
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

    public void confirm() {
        if (!this.status.equals("PENDING")) {
            throw new IllegalStateException("Only pending orders can be confirmed");
        }

        this.status = "CONFIRMED";
        this.updatedAt = OffsetDateTime.now();
    }

    public void cancel() {
        if (this.status.equals("CANCELLED")) {
            throw new IllegalStateException("Order is already cancelled");
        }

        this.status = "CANCELLED";
        this.updatedAt = OffsetDateTime.now();
    }

    public void markDelivered() {
        if(!this.status.equals("CONFIRMED")) {
            throw new IllegalStateException("Order must be in CONFIRMED state before marking delivered");
        }
        this.status = "DELIVERED";
        this.updatedAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getCartId() {
        return cartId;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void updateTotalAmount(BigDecimal totalAmount) {
        if(totalAmount==null || totalAmount.signum()<0) {
            throw new IllegalArgumentException("Total amount cannot be negative");
        }
        this.totalAmount = totalAmount;
        this.updatedAt = OffsetDateTime.now();
    }
}