package com.kart.order.cart.controller;

import com.kart.order.cart.dto.CartResponse;
import com.kart.order.cart.dto.CartSummaryResponse;
import com.kart.order.cart.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public CartResponse create() {
        return  cartService.createCart();
    }

    @GetMapping("/{cartId}")
    @ResponseStatus(HttpStatus.OK)
    public CartSummaryResponse getCart(@PathVariable("cartId") UUID cartId) {
        return  cartService.getCart(cartId);
    }
}
