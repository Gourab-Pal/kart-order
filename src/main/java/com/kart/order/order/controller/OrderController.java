package com.kart.order.order.controller;

import com.kart.order.order.dto.PlaceOrderRequest;
import com.kart.order.order.dto.PlaceOrderResponse;
import com.kart.order.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    @ResponseStatus(HttpStatus.CREATED)
    public PlaceOrderResponse placeOrder(@Valid @RequestBody PlaceOrderRequest placeOrderRequest) {
        return orderService.placeOrder(placeOrderRequest);
    }
}
