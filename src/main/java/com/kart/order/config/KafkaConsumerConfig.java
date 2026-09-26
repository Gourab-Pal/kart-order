package com.kart.order.config;

import com.kart.order.kafka.event.CatalogEvent;
import com.kart.order.kafka.event.DeliveryEvent;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CatalogEvent> catalogKafkaListenerContainerFactory(
            ConsumerFactory<String, CatalogEvent> consumerFactory,
            KafkaTemplate<Object, Object> kafkaTemplate
    ) {
        ConcurrentKafkaListenerContainerFactory<String, CatalogEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate, this::getDeadLetterTopic);
        recoverer.setLogRecoveryRecord(true);
        FixedBackOff fixedBackOff = new FixedBackOff(1000L, 2L);

        CommonErrorHandler errorHandler = new DefaultErrorHandler(recoverer, fixedBackOff);
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DeliveryEvent> deliveryKafkaListenerContainerFactory(
            ConsumerFactory<String, DeliveryEvent> consumerFactory,
            KafkaTemplate<Object, Object> kafkaTemplate
    ) {
        ConcurrentKafkaListenerContainerFactory<String, DeliveryEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate, this::getDeadLetterTopic);
        recoverer.setLogRecoveryRecord(true);
        FixedBackOff fixedBackOff = new FixedBackOff(1000L, 2L);

        CommonErrorHandler errorHandler = new DefaultErrorHandler(recoverer, fixedBackOff);
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }

    private TopicPartition getDeadLetterTopic(org.apache.kafka.clients.consumer.ConsumerRecord<?, ?> record, Exception exception) {
        return new TopicPartition(
                record.topic() + "-dlt",
                record.partition()
        );
    }
}
