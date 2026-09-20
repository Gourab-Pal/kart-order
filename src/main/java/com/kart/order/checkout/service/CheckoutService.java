package com.kart.order.checkout.service;

import com.kart.order.cart.entity.CartEntity;
import com.kart.order.cart.entity.CartItemEntity;
import com.kart.order.cart.exception.CartNotFoundException;
import com.kart.order.cart.exception.IllegalCartStateException;
import com.kart.order.cart.repository.CartItemRepository;
import com.kart.order.cart.repository.CartRepository;
import com.kart.order.checkout.dto.CheckoutCartResponse;
import com.kart.order.checkout.dto.CheckoutItemResponse;
import com.kart.order.checkout.exception.CheckoutException;
import com.kart.order.inventory.entity.InventoryEntity;
import com.kart.order.inventory.exception.InventoryNotFoundException;
import com.kart.order.inventory.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CheckoutService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final InventoryRepository inventoryRepository;

    public CheckoutService(CartItemRepository cartItemRepository, CartRepository cartRepository, InventoryRepository inventoryRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public CheckoutCartResponse proceedToCheckout(UUID cartId) {
        CartEntity cart = cartRepository.findById(cartId).orElseThrow(()-> new CartNotFoundException(cartId));
        if(!cart.isActive()) {
            throw new IllegalCartStateException(cartId, "ACTIVE", cart.getStatus());
        }
        List<CartItemEntity> cartItems = cartItemRepository.findAllByCartId(cartId);
        if(cartItems.isEmpty()) {
            throw new CheckoutException(cartId, "Cart is empty, can not proceed to checkout");
        }

        List<CheckoutItemResponse> checkoutItemResponses = new ArrayList<>();

        for(CartItemEntity cartItem : cartItems) {
            UUID productId = cartItem.getProductId();
            InventoryEntity inventory = inventoryRepository.findByProductId(productId).orElseThrow(()-> new InventoryNotFoundException(productId));
            CheckoutItemResponse response = CheckoutItemResponse.from(productId, cartItem.getQuantity(), inventory.getAvailableQuantity());
            checkoutItemResponses.add(response);
        }

        return CheckoutCartResponse.from(cart, checkoutItemResponses);
    }
}
