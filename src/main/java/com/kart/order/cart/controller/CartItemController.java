package com.kart.order.cart.controller;

import com.kart.order.cart.dto.CartItemCreateRequest;
import com.kart.order.cart.dto.CartItemResponse;
import com.kart.order.cart.service.CartItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
public class CartItemController {

    private final CartItemService cartItemService;
    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping("/{cartId}/items/create")
    @ResponseStatus(HttpStatus.CREATED)
    public CartItemResponse createCartItem(
            @PathVariable UUID cartId,
            @Valid @RequestBody CartItemCreateRequest cartItemCreateRequest
            ) {
        return cartItemService.createCartItem(cartId, cartItemCreateRequest);
    }
}
