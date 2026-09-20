package com.kart.order.cart.entity;

import com.kart.order.cart.exception.IllegalCartStateException;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "carts", schema = "kart_order")
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected CartEntity() {}

    public CartEntity(String status) {
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void checkout() {
        this.status = "CHECKED_OUT";
        this.updatedAt = OffsetDateTime.now();
    }

    public void abandon() {
        if(!this.status.equals("ACTIVE")) {
            throw new IllegalCartStateException(this.id, "ACTIVE", this.status);
        }
        this.status = "ABANDONED";
        this.updatedAt = OffsetDateTime.now();
    }

    public boolean isActive() {
        return this.status.equals("ACTIVE");
    }

    public void touch() {
        this.updatedAt = OffsetDateTime.now();
    }

}
