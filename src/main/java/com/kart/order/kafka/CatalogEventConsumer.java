package com.kart.order.kafka;

import com.kart.order.inventory.service.InventoryService;
import com.kart.order.kafka.event.CatalogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CatalogEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(CatalogEventConsumer.class);
    private final InventoryService inventoryService;

    public CatalogEventConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(
            topics = "${kafka.topic.catalog-events}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(CatalogEvent event) {
        if("PRODUCT_CREATED".equals(event.eventType())) {
            inventoryService.createInventoryFromProductCreatedEvent(event.productCreatedPayload().productId());
            logger.info("Inventory created for product id: {}", event.productCreatedPayload().productId());
        }
    }
}