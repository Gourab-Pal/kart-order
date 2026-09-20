package com.kart.order.cart.service;

import com.kart.order.cart.dto.CartItemCreateRequest;
import com.kart.order.cart.dto.CartItemQuantityUpdateRequest;
import com.kart.order.cart.dto.CartItemResponse;
import com.kart.order.cart.dto.CartItemSummaryResponse;
import com.kart.order.cart.entity.CartEntity;
import com.kart.order.cart.entity.CartItemEntity;
import com.kart.order.cart.exception.CartItemNotFoundException;
import com.kart.order.cart.exception.CartNotFoundException;
import com.kart.order.cart.exception.IllegalCartStateException;
import com.kart.order.cart.repository.CartItemRepository;
import com.kart.order.cart.repository.CartRepository;
import com.kart.order.catalog.client.CatalogClient;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CartItemService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CatalogClient catalogClient;

    public CartItemService(CartRepository cartRepository, CartItemRepository cartItemRepository, CatalogClient catalogClient) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.catalogClient = catalogClient;
    }

    @Transactional
    public CartItemResponse createCartItem(UUID cartId, CartItemCreateRequest cartItemCreateRequest) {
        CartEntity cart = cartRepository.findById(cartId).orElseThrow(()-> new CartNotFoundException(cartId));
        if(!cart.isActive()) {
            throw new IllegalCartStateException(cartId, "ACTIVE", cart.getStatus());
        }
        catalogClient.verifyProductExists(cartItemCreateRequest.productId());

        Optional<CartItemEntity> cartItem = cartItemRepository.findByCartIdAndProductId(cartId, cartItemCreateRequest.productId());
        if(cartItem.isPresent()) {
           CartItemEntity existingCartItem = cartItem.get();
           existingCartItem.increaseQuantity(cartItemCreateRequest.quantity());
           cart.touch();
           return CartItemResponse.from(existingCartItem);
        }

        CartItemEntity freshCartItem = new CartItemEntity(cart, cartItemCreateRequest.productId(), cartItemCreateRequest.quantity());
        cart.touch();
        return CartItemResponse.from(cartItemRepository.save(freshCartItem));
    }

    @Transactional
    public CartItemSummaryResponse updateCartItemQuantity(UUID cartId, UUID productId, CartItemQuantityUpdateRequest cartItemQuantityUpdateRequest) {
        CartEntity cart = cartRepository.findById(cartId).orElseThrow(()-> new CartNotFoundException(cartId));
        if(!cart.isActive()) {
            throw new IllegalCartStateException(cartId, "ACTIVE", cart.getStatus());
        }
        Optional<CartItemEntity> cartItem = cartItemRepository.findByCartIdAndProductId(cartId, productId);
        if(cartItem.isEmpty()) {
            throw new CartItemNotFoundException(cartId, productId);
        }
        CartItemEntity cartItemEntity = cartItem.get();
        cartItemEntity.updateItemQuantity(cartItemQuantityUpdateRequest.quantity());
        cart.touch();
        return CartItemSummaryResponse.from(cartItemEntity);
    }
}
