package com.kart.order.cart.service;

import com.kart.order.cart.dto.CartResponse;
import com.kart.order.cart.entity.CartEntity;
import com.kart.order.cart.repository.CartItemRepository;
import com.kart.order.cart.repository.CartRepository;
import com.kart.order.catalog.client.CatalogClient;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, CatalogClient catalogClient) {
        this.cartRepository = cartRepository;
    }

    @Transactional
    public CartResponse createCart() {
        CartEntity entity = new CartEntity("ACTIVE");
        return CartResponse.from(cartRepository.save(entity));
    }
}
