package com.kart.order.catalog.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductResponse(
        UUID id,
        String name,
        String sku,
        BigDecimal price
) {
}
