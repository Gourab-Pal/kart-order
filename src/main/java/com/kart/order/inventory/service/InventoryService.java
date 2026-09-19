package com.kart.order.inventory.service;

import com.kart.order.catalog.client.CatalogClient;
import com.kart.order.catalog.exception.ProductNotFoundException;
import com.kart.order.inventory.dto.*;
import com.kart.order.inventory.entity.InventoryEntity;
import com.kart.order.inventory.exception.InventoryAlreadyExistsException;
import com.kart.order.inventory.exception.InventoryNotFoundException;
import com.kart.order.inventory.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    @Transactional
    public InventoryResponse findInventory(UUID productId) {
        InventoryEntity entity = inventoryRepository.findByProductId(productId).orElseThrow(() -> new InventoryNotFoundException(productId));
        return InventoryResponse.from(entity);
    }

    @Transactional
    public InventoryResponse restock(UUID productId, InventoryRestockRequest request) {
        InventoryEntity entity = inventoryRepository.findByProductId(productId).orElseThrow(() -> new InventoryNotFoundException(productId));
        entity.restock(request.quantity());
        return InventoryResponse.from(inventoryRepository.save(entity));
    }

    @Transactional
    public InventoryResponse reserve(UUID productId, InventoryReserveRequest request) {
        InventoryEntity entity = inventoryRepository.findByProductId(productId).orElseThrow(() -> new InventoryNotFoundException(productId));
        entity.reserve(request.quantity());
        return InventoryResponse.from(inventoryRepository.save(entity));
    }

    @Transactional
    public InventoryResponse release(UUID productId, InventoryReleaseRequest request) {
        InventoryEntity entity = inventoryRepository.findByProductId(productId).orElseThrow(() -> new InventoryNotFoundException(productId));
        entity.release(request.quantity());
        return InventoryResponse.from(inventoryRepository.save(entity));
    }
}
