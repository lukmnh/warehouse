package com.geli.warehouse.service.impl;

import com.geli.warehouse.dto.response.StockResponse;
import com.geli.warehouse.model.Stock;
import com.geli.warehouse.repository.ItemsRepository;
import com.geli.warehouse.repository.StockRepository;
import com.geli.warehouse.repository.VariantRepository;
import com.geli.warehouse.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;
    private final ItemsRepository itemsRepository;
    private final VariantRepository variantRepository;

    @Override
    public List<StockResponse> getByItem(UUID itemId) {
        if (!itemsRepository.existsById(itemId)) {
            throw new IllegalArgumentException("Items not found with ID: " + itemId);
        }
        return stockRepository.findByItemIdOrderByCreatedAtDesc(itemId).stream()
                .map(this::setTheResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockResponse> getByVariant(UUID variantId) {
        if (!variantRepository.existsById(variantId)) {
            throw new IllegalArgumentException("Variant not found with ID: " + variantId);
        }
        return stockRepository.findByVariantIdOrderByCreatedAtDesc(variantId).stream()
                .map(this::setTheResponse)
                .collect(Collectors.toList());
    }

    private StockResponse setTheResponse(Stock stock) {
        return StockResponse.builder()
                .id(stock.getId())
                .itemId(stock.getItem().getId())
                .itemName(stock.getItem().getName())
                .itemCode(stock.getItem().getProductCode())
                .variantId(stock.getVariant() != null ? stock.getVariant().getId() : null)
                .variantName(stock.getVariant() != null ? stock.getVariant().getVariantName() : null)
                .variantValue(stock.getVariant() != null ? stock.getVariant().getVariantValue() : null)
                .stockType(stock.getMovementType())
                .quantityBefore(stock.getQuantityBefore())
                .quantityChange(stock.getQuantityChange())
                .quantityAfter(stock.getQuantityAfter())
                .notes(stock.getNotes())
                .createdAt(stock.getCreatedAt())
                .build();
    }
}
