package com.geli.warehouse.service.impl;

import com.geli.warehouse.constant.StockMovementEnum;
import com.geli.warehouse.dto.request.ItemsRequest;
import com.geli.warehouse.dto.request.StockReduceRequest;
import com.geli.warehouse.dto.response.ItemsResponse;
import com.geli.warehouse.model.*;
import com.geli.warehouse.repository.BrandsRepository;
import com.geli.warehouse.repository.CategoriesRepository;
import com.geli.warehouse.repository.ItemsRepository;
import com.geli.warehouse.repository.StockRepository;
import com.geli.warehouse.service.ItemsService;
import com.geli.warehouse.util.ProductGenerator;
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
public class ItemsServiceImpl implements ItemsService {

    private final ItemsRepository itemsRepository;
    private final CategoriesRepository categoriesRepository;
    private final BrandsRepository brandsRepository;
    private final ProductGenerator codeGenerator;
    private final StockRepository stockRepository;

    @Override
    public List<ItemsResponse> getAll() {
        return itemsRepository.findAll().stream()
                .map(this::setTheResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ItemsResponse getById(UUID id) {
        Items item = itemsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        return setTheResponse(item);
    }


    @Override
    @Transactional
    public ItemsResponse create(ItemsRequest request) {
        log.info("Creating new item: {}", request.getName());

        // validasi
        if (request.getCategoryId() != null) {
            if (!categoriesRepository.existsById(request.getCategoryId())) {
                throw new IllegalArgumentException("Categories not found with ID: " + request.getCategoryId());
            }
        }

        if (request.getBrandId() != null) {
            if (!brandsRepository.existsById(request.getBrandId())) {
                throw new IllegalArgumentException("Brands not found with ID: " + request.getBrandId());
            }
        }

        String productCode = generateProductCode(request);

        Items items = Items.builder()
                .productCode(productCode)
                .name(request.getName().trim())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getInitialStock() != null ? request.getInitialStock() : 0)
                .warehouseZone(request.getWarehouseZone())
                .rackNumber(request.getRackNumber())
                .build();

        if (request.getCategoryId() != null) {
            Categories category = categoriesRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            items.setCategory(category);
        }

        if (request.getBrandId() != null) {
            Brands brand = brandsRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new RuntimeException("Brand not found"));
            items.setBrand(brand);
        }

        Items saved = itemsRepository.save(items);
        log.info("Item created: {} - {}", saved.getProductCode(), saved.getName());

        if(saved.getStock() > 0) {
            stockRecord(saved, null, StockMovementEnum.INITIAL_STOCK, 0, saved.getStock(), "Initial stock when item created");
        }
        return setTheResponse(saved);
    }

    @Override
    @Transactional
    public ItemsResponse update(UUID id, ItemsRequest request) {
        log.info("inside update() Updating item with id: {}", id);

        // validasi
        if (!itemsRepository.existsById(id)) {
            throw new IllegalArgumentException("Items not found with ID: " + id);
        }

        if (request.getCategoryId() != null) {
            if (!categoriesRepository.existsById(request.getCategoryId())) {
                throw new IllegalArgumentException("Categories not found with ID: " + request.getCategoryId());
            }
        }

        if (request.getBrandId() != null) {
            if (!brandsRepository.existsById(request.getBrandId())) {
                throw new IllegalArgumentException("Brands not found with ID: " + request.getBrandId());
            }
        }

        if (request.getName() == null && request.getDescription() == null &&
                request.getPrice() == null && request.getCategoryId() == null &&
                request.getBrandId() == null && request.getWarehouseZone() == null &&
                request.getRackNumber() == null && request.getStockChange() == null) {
            throw new IllegalArgumentException("Must update 1 field to execute update service");
        }

        Items item = itemsRepository.findById(id).get();

        if (request.getName() != null) item.setName(request.getName().trim());
        if (request.getDescription() != null) item.setDescription(request.getDescription());
        if (request.getPrice() != null) item.setPrice(request.getPrice());
        if (request.getWarehouseZone() != null) item.setWarehouseZone(request.getWarehouseZone());
        if (request.getRackNumber() != null) item.setRackNumber(request.getRackNumber());

        if (request.getStockChange() != null && request.getStockChange() > 0) {
            int stockBefore = item.getStock();
            item.addStock(request.getStockChange());
            log.info("Stock added: {} (+{}), New stock: {}",
                    item.getProductCode(), request.getStockChange(), item.getStock());
            stockRecord(item, null, StockMovementEnum.STOCK_IN, stockBefore, request.getStockChange(), "Stock in via update");
        }

        if (request.getCategoryId() != null) {
            categoriesRepository.findById(request.getCategoryId()).ifPresent(item::setCategory);
        }
        if (request.getBrandId() != null) {
            brandsRepository.findById(request.getBrandId()).ifPresent(item::setBrand);
        }

        Items updated = itemsRepository.save(item);
        log.info("Item updated: {}", updated.getProductCode());

        return setTheResponse(updated);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!itemsRepository.existsById(id)) {
            throw new IllegalArgumentException("Items not found");
        }
        itemsRepository.softDeleteById(id);
        log.info("Items soft deleted with id: {}", id);
    }

    @Override
    @Transactional
    public ItemsResponse reduceStock(UUID id, StockReduceRequest request) {
        log.info("inside reduceStock() Reducing stock for item id: {}", id);

        Items item = itemsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Items not found with ID: " + id));

        int stockBefore = item.getStock();
        item.reduceStock(request.getQuantity());
        log.info("Stock reduced: {} (-{}), New stock: {}",
                item.getProductCode(), request.getQuantity(), item.getStock());

        Items updated = itemsRepository.save(item);
        stockRecord(updated, null, StockMovementEnum.STOCK_OUT, stockBefore, request.getQuantity(), "Stock out via (reducing stock)");

        return setTheResponse(updated);
    }

    private ItemsResponse setTheResponse(Items item) {
        return ItemsResponse.builder()
                .id(item.getId())
                .productCode(item.getProductCode())
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .stock(item.getStock())
                .warehouseZone(item.getWarehouseZone())
                .rackNumber(item.getRackNumber())
                .categoryId(item.getCategory() != null ? item.getCategory().getId() : null)
                .categoryName(item.getCategoryName())
                .brandId(item.getBrand() != null ? item.getBrand().getId() : null)
                .brandName(item.getBrandName())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }

    private String generateProductCode(ItemsRequest request) {
        if (request.getBrandId() != null) {
            Brands brand = brandsRepository.findById(request.getBrandId()).orElse(null);
            if (brand != null && request.getCategoryId() != null) {
                Categories category = categoriesRepository.findById(request.getCategoryId()).orElse(null);
                if (category != null) {
                    return codeGenerator.generate(brand.getCode(), category.getCode());
                }
            }
        }
        return codeGenerator.generateDefault();
    }

    private void stockRecord(Items item, Variant variant,
                                StockMovementEnum type, int stockBefore, int quantityChange, String notes) {
        Stock stock = Stock.builder()
                .item(item)
                .variant(variant)
                .movementType(type)
                .quantityBefore(stockBefore)
                .quantityChange(quantityChange)
                .quantityAfter(stockBefore + (type == StockMovementEnum.STOCK_OUT ? -quantityChange : quantityChange))
                .notes(notes)
                .build();
        stockRepository.save(stock);
    }
}
