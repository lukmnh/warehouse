package com.geli.warehouse.service;

import com.geli.warehouse.dto.response.StockResponse;
import com.geli.warehouse.repository.StockRepository;

import java.util.List;
import java.util.UUID;

public interface StockService {
    List<StockResponse> getByItem(UUID itemId);
    List<StockResponse> getByVariant(UUID variantId);
}
