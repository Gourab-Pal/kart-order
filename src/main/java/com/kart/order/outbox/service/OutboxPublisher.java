package com.kart.order.outbox.service;

import com.kart.order.kafka.event.CatalogEvent;
import com.kart.order.outbox.entity.OutboxEventEntity;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class OutboxPublisher {

    private static final Logger logger = LoggerFactory.getLogger(OutboxPublisher.class);
    private final OutboxClaimService outboxClaimService;
    private final KafkaTemplate<String, CatalogEvent> kafkaTemplate;
    private final String catalogEventsTopic;

    public OutboxPublisher(
            OutboxClaimService outboxClaimService,
            KafkaTemplate<String, CatalogEvent> kafkaTemplate,
            @Value("${kafka.topic.catalog-events}") String catalogEventsTopic
    ) {
        this.outboxClaimService = outboxClaimService;
        this.kafkaTemplate = kafkaTemplate;
        this.catalogEventsTopic = catalogEventsTopic;
    }

    @Scheduled(fixedDelay = 5000)
    public void publishPendingEvents() {
        List<OutboxEventEntity> events = outboxClaimService.claimDueEvents();
        for (OutboxEventEntity outboxEvent : events) {
            publish(outboxEvent);
        }
    }

    private void publish(OutboxEventEntity outboxEvent) {
        try {
            CatalogEvent event = new CatalogEvent(
                    outboxEvent.getId(),
                    outboxEvent.getEventType(),
                    outboxEvent.getEventVersion(),
                    outboxEvent.getCreatedAt(),
                    outboxEvent.getPayload()
            );

            ProducerRecord<String, CatalogEvent> record = new ProducerRecord<>(
                    catalogEventsTopic,
                    outboxEvent.getAggregateId().toString(),
                    event
            );

            record.headers().add(
                    "source-service",
                    "kart-catalog".getBytes(StandardCharsets.UTF_8)
            );

            kafkaTemplate.send(record).get(10, TimeUnit.SECONDS);

            outboxClaimService.markPublished(outboxEvent.getId());

            logger.info("Published outbox event {} of type {}", outboxEvent.getId(), outboxEvent.getEventType());
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            recordFailure(outboxEvent, exception);
        } catch (Exception exception) {
            recordFailure(outboxEvent, exception);
        }
    }

    private void recordFailure(OutboxEventEntity outboxEvent, Exception exception) {
        String error = exception.getMessage();

        if (error == null || error.isBlank()) {
            error = exception.getClass().getSimpleName();
        }

        outboxClaimService.recordPublishFailure(
                outboxEvent.getId(),
                error
        );

        logger.warn("Failed to publish outbox event {}. Error: {}", outboxEvent.getId(), error);
    }
}