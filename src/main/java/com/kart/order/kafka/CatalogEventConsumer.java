package com.kart.order.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.kart.order.inventory.service.InventoryService;
import com.kart.order.kafka.event.CatalogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CatalogEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(CatalogEventConsumer.class);
    private final InventoryService inventoryService;

    public CatalogEventConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(
            topics = "${kafka.topic.catalog-events}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "catalogKafkaListenerContainerFactory"
    )
    public void consume(CatalogEvent event) {
        switch(event.eventType()) {
            case "PRODUCT_CREATED" ->
                handleProductCreated(event);
            default ->
                throw new IllegalStateException("Unsupported catalog event type: " + event.eventType());
        }
    }

    private void handleProductCreated(CatalogEvent event) {
        JsonNode payload = event.payload();
        UUID productId = UUID.fromString(payload.get("productId").asText());
        inventoryService.createInventoryFromProductCreatedEvent(productId);
        logger.info("Inventory created for product id: {}", productId);
    }
}