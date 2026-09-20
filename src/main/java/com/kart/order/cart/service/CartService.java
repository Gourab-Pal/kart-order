package com.kart.order.cart.service;

import com.kart.order.cart.dto.CartResponse;
import com.kart.order.cart.dto.CartSummaryResponse;
import com.kart.order.cart.entity.CartEntity;
import com.kart.order.cart.entity.CartItemEntity;
import com.kart.order.cart.exception.CartNotFoundException;
import com.kart.order.cart.repository.CartItemRepository;
import com.kart.order.cart.repository.CartRepository;
import com.kart.order.catalog.client.CatalogClient;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public CartResponse createCart() {
        CartEntity entity = new CartEntity("ACTIVE");
        return CartResponse.from(cartRepository.save(entity));
    }

    @Transactional
    public CartSummaryResponse getCart(UUID cartId) {
        CartEntity cart = cartRepository.findById(cartId).orElseThrow(()-> new CartNotFoundException(cartId));
        List<CartItemEntity> items = cartItemRepository.findAllByCartId(cartId);
        return  CartSummaryResponse.from(cart, items);
    }

    @Transactional
    public CartResponse abandon(UUID cartId) {
        CartEntity cart =  cartRepository.findById(cartId).orElseThrow(()-> new CartNotFoundException(cartId));
        cart.abandon();
        return CartResponse.from(cart);
    }
}
