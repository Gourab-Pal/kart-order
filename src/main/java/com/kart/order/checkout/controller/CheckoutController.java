package com.kart.order.checkout.controller;

import com.kart.order.checkout.dto.CheckoutCartResponse;
import com.kart.order.checkout.service.CheckoutService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;
    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @GetMapping("/{cartId}")
    public CheckoutCartResponse getStockSummary(@PathVariable UUID cartId) {
        return checkoutService.getStockSummary(cartId);
    }
}
