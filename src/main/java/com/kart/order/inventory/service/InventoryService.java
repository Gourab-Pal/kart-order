package com.kart.order.inventory.service;

import com.kart.order.catalog.client.CatalogClient;
import com.kart.order.catalog.exception.ProductNotFoundException;
import com.kart.order.inventory.dto.InventoryCreateRequest;
import com.kart.order.inventory.dto.InventoryResponse;
import com.kart.order.inventory.entity.InventoryEntity;
import com.kart.order.inventory.exception.InventoryAlreadyExistsException;
import com.kart.order.inventory.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final CatalogClient catalogClient;

    public InventoryService(InventoryRepository inventoryRepository, CatalogClient catalogClient) {
        this.inventoryRepository = inventoryRepository;
        this.catalogClient = catalogClient;
    }

    @Transactional
    public InventoryResponse createInventory(InventoryCreateRequest request) {
        catalogClient.verifyProductExists(request.productId());
        if(inventoryRepository.existsByProductId(request.productId())) {
            throw new InventoryAlreadyExistsException(request.productId());
        }
        InventoryEntity inventoryEntity = new InventoryEntity(
                request.productId(),
                request.availableQuantity()
        );
        InventoryEntity savedEntity = inventoryRepository.save(inventoryEntity);
        return InventoryResponse.from(savedEntity);
    }
}
