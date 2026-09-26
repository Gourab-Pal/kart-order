package com.kart.order.kafka.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.kart.order.inventory.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeliveryEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryEventConsumer.class);
    private final InventoryService inventoryService;

    public DeliveryEventConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(
            topics = "${kafka.topic.delivery-events}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "deliveryKafkaListenerContainerFactory"
    )
    public void consume(DeliveryEvent event) {
        switch (event.eventType()) {
            case "PRODUCTS_DELIVERED" ->
                handleProductsDelivered(event);
            default ->
                throw new IllegalArgumentException("Unsupported delivery event type: " + event.eventType());
        }
    }

    private void handleProductsDelivered(DeliveryEvent event) {
        JsonNode payload = event.payload();
        UUID orderId = UUID.fromString(payload.get("orderId").asText());
        inventoryService.createInventoryFromProductCreatedEvent(orderId);
        logger.info("Order consumed for products with orderId: {}", orderId);
    }
}
