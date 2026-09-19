package com.kart.order.cart.service;

import com.kart.order.cart.dto.CartCreateRequest;
import com.kart.order.cart.dto.CartResponse;
import com.kart.order.cart.entity.CartEntity;
import com.kart.order.cart.repository.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Transactional
    public CartResponse createCart(CartCreateRequest cartCreateRequest) {
        CartEntity entity = new CartEntity(
                cartCreateRequest.status()
        );
        return CartResponse.from(cartRepository.save(entity));
    }
}
