package com.kart.order.catalog.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class CatalogClient {

    private final RestClient catalogRestClient;

    public CatalogClient(RestClient catalogRestClient) {
        this.catalogRestClient = catalogRestClient;
    }

    public void verifyProductExists(UUID productId) {
        catalogRestClient
                .get()
                .uri("/api/v1/products/{productId}", productId)
                .retrieve()
                .toBodilessEntity();
    }
}
