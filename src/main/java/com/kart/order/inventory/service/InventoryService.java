package com.kart.order.inventory.service;

import com.kart.order.catalog.client.CatalogClient;
import com.kart.order.inventory.dto.*;
import com.kart.order.inventory.entity.InventoryEntity;
import com.kart.order.inventory.exception.InventoryAlreadyExistsException;
import com.kart.order.inventory.exception.InventoryNotFoundException;
import com.kart.order.inventory.repository.InventoryRepository;
import com.kart.order.order.entity.OrderItemEntity;
import com.kart.order.order.exception.OrderException;
import com.kart.order.order.repository.OrderItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final CatalogClient catalogClient;
    private final OrderItemRepository orderItemRepository;

    public InventoryService(
            InventoryRepository inventoryRepository,
            CatalogClient catalogClient,
            OrderItemRepository orderItemRepository
    ) {
        this.inventoryRepository = inventoryRepository;
        this.catalogClient = catalogClient;
        this.orderItemRepository = orderItemRepository;
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
    public void createInventoryFromProductCreatedEvent(UUID productId) {
        if(inventoryRepository.existsByProductId(productId)) {
            return;
        }
        InventoryEntity inventoryEntity = new InventoryEntity(productId, 0);
        inventoryRepository.save(inventoryEntity);
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

    @Transactional
    public InventoryResponse consume(UUID productId, InventoryConsumeRequest request) {
        InventoryEntity entity = inventoryRepository.findByProductId(productId).orElseThrow(() -> new InventoryNotFoundException(productId));
        entity.consume(request.quantity());
        return InventoryResponse.from(inventoryRepository.save(entity));
    }

    @Transactional
    public void consumeQuantityFromDeliveredEvent(UUID orderId) {
        List<OrderItemEntity> orderItems = orderItemRepository.findAllByOrderId(orderId);
        if(orderItems.isEmpty()) {
            throw new OrderException("No order has been created with orderId: " + orderId);
        }

        for(OrderItemEntity orderItem : orderItems) {
            UUID productId = orderItem.getProductId();
            int quantity = orderItem.getQuantity();
            InventoryEntity entity = inventoryRepository.findByProductId(productId).orElseThrow(() -> new InventoryNotFoundException(productId));
            entity.consume(quantity);
        }
    }
}
