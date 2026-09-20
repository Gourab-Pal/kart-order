package com.kart.order.inventory.entity;

import jakarta.persistence.*;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "inventory", schema = "kart_order")
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "product_id", nullable = false, unique = true)
    private UUID productId;

    @Column(name = "available_quantity", nullable = false)
    private int availableQuantity;

    @Column(name = "reserved_quantity", nullable = false)
    private int reservedQuantity;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected InventoryEntity() {
    }

    public InventoryEntity(
            UUID productId,
            int availableQuantity
    ) {
        if (availableQuantity < 0) {
            throw new IllegalArgumentException("Available quantity cannot be negative");
        }
        this.productId = productId;
        this.availableQuantity = availableQuantity;
        this.reservedQuantity = 0;
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

    public UUID getProductId() {
        return productId;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public int getReservedQuantity() {
        return reservedQuantity;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void restock(int quantity) {
        this.availableQuantity = this.availableQuantity + quantity;
        this.updatedAt = OffsetDateTime.now();
    }

    public void reserve(int quantity) {
        if(this.availableQuantity < quantity) {
            throw new DataIntegrityViolationException("Requested quantity exceeded available stock");
        }
        this.availableQuantity = this.availableQuantity - quantity;
        this.reservedQuantity = this.reservedQuantity + quantity;
        this.updatedAt = OffsetDateTime.now();
    }

    public void release(int quantity) {
        if(this.reservedQuantity < quantity) {
            throw new DataIntegrityViolationException("Released quantity exceeded reserved stock");
        }
        this.availableQuantity = this.availableQuantity + quantity;
        this.reservedQuantity = this.reservedQuantity - quantity;
        this.updatedAt = OffsetDateTime.now();
    }

    public void consume(int quantity) {
        if(this.reservedQuantity < quantity) {
            throw new DataIntegrityViolationException("Consume quantity exceeded reserved stock");
        }
        this.reservedQuantity = this.reservedQuantity - quantity;
        this.updatedAt = OffsetDateTime.now();
    }

    public void validateQuantityUpdateRequest(int quantity) {
        if(this.availableQuantity < quantity) {
            throw new DataIntegrityViolationException("Consume quantity " + quantity +  " exceeded available stock " + this.availableQuantity);
        }
    }
}