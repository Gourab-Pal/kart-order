package com.kart.order.order.dto;

import com.kart.order.order.entity.OrderEntity;
import com.kart.order.order.entity.OrderItemEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record PlaceOrderResponse(
        UUID orderId,
        String orderStatus,
        UUID cartId,
        BigDecimal orderAmount,
        String currency,
        List<OrderItemDetailsResponse> items,
        String message,
        OffsetDateTime timestamp
) {
    public static PlaceOrderResponse from(OrderEntity order, List<OrderItemEntity> items) {

        List<OrderItemDetailsResponse> itemDetails = new ArrayList<>();
        for (OrderItemEntity orderItemEntity : items) {
            itemDetails.add(OrderItemDetailsResponse.from(orderItemEntity));
        }

        return new PlaceOrderResponse(
                order.getId(),
                order.getStatus(),
                order.getCartId(),
                order.getTotalAmount(),
                order.getCurrency(),
                itemDetails,
                "Order placed successfully",
                OffsetDateTime.now()
        );
    }
}
