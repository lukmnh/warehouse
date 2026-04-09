package com.geli.warehouse.service.impl;

import com.geli.warehouse.dto.request.StockReduceRequest;
import com.geli.warehouse.dto.request.VariantRequest;
import com.geli.warehouse.dto.response.VariantResponse;
import com.geli.warehouse.model.Items;
import com.geli.warehouse.model.Variant;
import com.geli.warehouse.repository.ItemsRepository;
import com.geli.warehouse.repository.VariantRepository;
import com.geli.warehouse.service.VariantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VariantServiceImpl implements VariantService {
    private final VariantRepository variantRepository;
    private final ItemsRepository itemsRepository;

    @Override
    public List<VariantResponse> getAll() {
        return variantRepository.findAll().stream()
                .map(this::setTheResponse)
                .collect(Collectors.toList());
    }

    @Override
    public VariantResponse getById(UUID id) {
        Variant variant = variantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Variant not found"));
        return setTheResponse(variant);
    }

    @Override
    public List<VariantResponse> getByItem(UUID itemId) {
        if (!itemsRepository.existsById(itemId)) {
            throw new IllegalArgumentException("Items not found with ID: " + itemId);
        }
        return variantRepository.findActiveByItemId(itemId).stream()
                .map(this::setTheResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VariantResponse create(UUID itemId, VariantRequest request) {
        log.info("Creating new variant for item id: {}", itemId);

        Items item = itemsRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Items not found with ID: " + itemId));

        if (variantRepository.existsByItemIdAndVariantNameAndVariantValue(
                itemId, request.getVariantName(), request.getVariantValue())) {
            throw new IllegalArgumentException(
                    "Variant already exists: " + request.getVariantName() + " - " + request.getVariantValue());
        }

        Variant variant = Variant.builder()
                .item(item)
                .variantName(request.getVariantName().trim())
                .variantValue(request.getVariantValue().trim())
                .price(request.getPrice())
                .stock(request.getInitialStock() != null ? request.getInitialStock() : 0)
                .warehouseZone(request.getWarehouseZone())
                .rackNumber(request.getRackNumber())
                .build();

        Variant saved = variantRepository.save(variant);
        log.info("Variant created: {} - {} for item: {}", saved.getVariantName(), saved.getVariantValue(), item.getProductCode());

        return setTheResponse(saved);
    }

    @Override
    @Transactional
    public VariantResponse update(UUID id, VariantRequest request) {
        log.info("inside update() Updating variant with id: {}", id);

        Variant variant = variantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Variant not found with ID: " + id));

        if (request.getVariantName() == null && request.getVariantValue() == null &&
                request.getPrice() == null && request.getWarehouseZone() == null &&
                request.getRackNumber() == null && request.getStockChange() == null) {
            throw new IllegalArgumentException("Must update 1 field to execute update service");
        }

        if (request.getVariantName() != null) variant.setVariantName(request.getVariantName().trim());
        if (request.getVariantValue() != null) variant.setVariantValue(request.getVariantValue().trim());
        if (request.getPrice() != null) variant.setPrice(request.getPrice());
        if (request.getWarehouseZone() != null) variant.setWarehouseZone(request.getWarehouseZone());
        if (request.getRackNumber() != null) variant.setRackNumber(request.getRackNumber());

        if (request.getStockChange() != null && request.getStockChange() > 0) {
            variant.addStock(request.getStockChange());
            log.info("Stock added: {}-{} (+{}), New stock: {}",
                    variant.getVariantName(), variant.getVariantValue(),
                    request.getStockChange(), variant.getStock());
        }

        Variant updated = variantRepository.save(variant);
        log.info("Variant updated: {} - {}", updated.getVariantName(), updated.getVariantValue());

        return setTheResponse(updated);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!variantRepository.existsById(id)) {
            throw new IllegalArgumentException("Variant not found");
        }
        variantRepository.softDeleteById(id);
        log.info("Variant soft deleted with id: {}", id);
    }

    @Override
    @Transactional
    public VariantResponse reduceStock(UUID id, StockReduceRequest request) {
        log.info("inside reduceStock() Reducing stock for variant id: {}", id);

        Variant variant = variantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Variant not found with ID: " + id));

        variant.reduceStock(request.getQuantity());
        log.info("Stock reduced: {}-{} (-{}), New stock: {}",
                variant.getVariantName(), variant.getVariantValue(),
                request.getQuantity(), variant.getStock());

        Variant updated = variantRepository.save(variant);
        return setTheResponse(updated);
    }

    private VariantResponse setTheResponse(Variant variant) {
        return VariantResponse.builder()
                .id(variant.getId())
                .itemId(variant.getItem().getId())
                .itemName(variant.getItem().getName())
                .itemCode(variant.getItem().getProductCode())
                .variantName(variant.getVariantName())
                .variantValue(variant.getVariantValue())
                .price(variant.getEffectivePrice())
                .stock(variant.getStock())
                .warehouseZone(variant.getWarehouseZone())
                .rackNumber(variant.getRackNumber())
                .createdAt(variant.getCreatedAt())
                .updatedAt(variant.getUpdatedAt())
                .build();
    }
}
