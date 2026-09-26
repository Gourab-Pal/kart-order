package com.kart.order.order.service;

import com.kart.order.cart.entity.CartEntity;
import com.kart.order.cart.entity.CartItemEntity;
import com.kart.order.cart.exception.CartNotFoundException;
import com.kart.order.cart.exception.IllegalCartStateException;
import com.kart.order.cart.repository.CartItemRepository;
import com.kart.order.cart.repository.CartRepository;
import com.kart.order.catalog.client.CatalogClient;
import com.kart.order.catalog.dto.ProductResponse;
import com.kart.order.checkout.exception.CheckoutException;
import com.kart.order.inventory.entity.InventoryEntity;
import com.kart.order.inventory.exception.InventoryNotFoundException;
import com.kart.order.inventory.repository.InventoryRepository;
import com.kart.order.kafka.event.OrderCancelledPayload;
import com.kart.order.kafka.event.OrderConfirmedPayload;
import com.kart.order.order.dto.PlaceOrderRequest;
import com.kart.order.order.dto.OrderResponse;
import com.kart.order.order.entity.OrderEntity;
import com.kart.order.order.entity.OrderItemEntity;
import com.kart.order.order.exception.IllegalOrderStateException;
import com.kart.order.order.exception.OrderException;
import com.kart.order.order.exception.OrderNotFoundException;
import com.kart.order.order.repository.OrderItemRepository;
import com.kart.order.order.repository.OrderRepository;
import com.kart.order.outbox.service.OutboxEventService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CatalogClient catalogClient;
    private final InventoryRepository inventoryRepository;
    private final OutboxEventService outboxEventService;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            CatalogClient catalogClient,
            InventoryRepository inventoryRepository,
            OutboxEventService outboxEventService
            ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.catalogClient = catalogClient;
        this.inventoryRepository = inventoryRepository;
        this.outboxEventService = outboxEventService;
    }

    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest placeOrderRequest) {

        // find and validate cart
        CartEntity cart = cartRepository.findById(placeOrderRequest.cartId()).orElseThrow(()-> new CartNotFoundException(placeOrderRequest.cartId()));
        if(!cart.isActive()) {
            throw new IllegalCartStateException(placeOrderRequest.cartId(), "ACTIVE", cart.getStatus());
        }

        // validate cart has items
        List<CartItemEntity> cartItems = cartItemRepository.findAllByCartId(cart.getId());
        if(cartItems.isEmpty()) {
            throw new CheckoutException(cart.getId(), "Cart is empty, can't place order");
        }

        // find product details from catalog service
        List<ProductResponse> products = new ArrayList<>();
        for(CartItemEntity cartItem : cartItems) {
            products.add(catalogClient.getProductById(cartItem.getProductId()));
        }

        // create empty order with PENDING status
        if(orderRepository.existsByCartId(cart.getId())) {
            throw new CheckoutException(cart.getId(), "Order already exists for the given cart");
        }
        OrderEntity order = new OrderEntity(cart.getId());
        orderRepository.save(order);

        // save order items to order, update running total amount and reserve stock
        BigDecimal totalAmount = BigDecimal.ZERO;
        for(int i = 0; i < products.size(); i++) {
            OrderItemEntity orderItem = new OrderItemEntity(
                    order,
                    products.get(i).id(),
                    products.get(i).name(),
                    products.get(i).sku(),

                    cartItems.get(i).getQuantity(),
                    products.get(i).price()
            );
            OrderItemEntity savedOrderItem = orderItemRepository.save(orderItem);
            totalAmount = totalAmount.add(savedOrderItem.getLineTotal());
            UUID productId = products.get(i).id();
            InventoryEntity inventory = inventoryRepository.findByProductId(productId).orElseThrow(()-> new InventoryNotFoundException(productId));
            inventory.reserve(cartItems.get(i).getQuantity());
        }

        // update totalAmount in order
        order.updateTotalAmount(totalAmount);

        // confirm order
        order.confirm();

        // mark cart as checked out
        cart.checkout();

        outboxEventService.saveEvent(
                "order",
                order.getId(),
                "ORDER_CONFIRMED",
                1,
                new OrderConfirmedPayload(order.getId(), cart.getId())
        );

        return OrderResponse.from(order, orderItemRepository.findAllByOrderId(order.getId()));
    }

    @Transactional
    public OrderResponse fetchOrder(UUID orderId) {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(()-> new OrderNotFoundException(orderId));
        return OrderResponse.from(order, orderItemRepository.findAllByOrderId(order.getId()));
    }

    @Transactional
    public OrderResponse cancelOrder(UUID orderId) {

        // validate order
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(()-> new OrderNotFoundException(orderId));
        if(!"CONFIRMED".equals(order.getStatus())) {
            throw new IllegalOrderStateException(orderId, "CONFIRMED", order.getStatus());
        }

        // load all order items
        List<OrderItemEntity> orderItems = orderItemRepository.findAllByOrderId(order.getId());
        if(orderItems.isEmpty()) {
            throw new OrderException("No order items found for order id " + orderId);
        }

        // release stock
        for(OrderItemEntity orderItem : orderItems) {
            InventoryEntity inventory = inventoryRepository.findByProductId(orderItem.getProductId()).orElseThrow(()-> new InventoryNotFoundException(orderItem.getProductId()));
            inventory.release(orderItem.getQuantity());
        }

        // cancel order
        order.cancel();

        outboxEventService.saveEvent(
                "order",
                order.getId(),
                "ORDER_CANCELLED",
                1,
                new OrderCancelledPayload(order.getId())
        );

        return OrderResponse.from(order, orderItemRepository.findAllByOrderId(order.getId()));
    }

    @Transactional
    public void markOrderAsDelivered(UUID orderId) {
        orderRepository.findById(orderId).orElseThrow(()-> new OrderNotFoundException(orderId));
        List<OrderItemEntity> orderItems = orderItemRepository.findAllByOrderId(orderId);
        for(OrderItemEntity orderItem : orderItems) {
            InventoryEntity inventory = inventoryRepository.findByProductId(orderItem.getProductId()).orElseThrow(()-> new InventoryNotFoundException(orderItem.getProductId()));
            inventory.consume(orderItem.getQuantity());
        }
    }
}
