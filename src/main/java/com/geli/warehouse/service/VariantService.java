package com.geli.warehouse.service;

import com.geli.warehouse.dto.request.StockReduceRequest;
import com.geli.warehouse.dto.request.VariantRequest;
import com.geli.warehouse.dto.response.VariantResponse;

import java.util.List;
import java.util.UUID;

public interface VariantService {
    List<VariantResponse> getAll();
    VariantResponse getById(UUID id);
    List<VariantResponse> getByItem(UUID itemId);
    VariantResponse create(UUID itemId, VariantRequest request);
    VariantResponse update(UUID id, VariantRequest request);
    void delete(UUID id);
    VariantResponse reduceStock(UUID id, StockReduceRequest request);
}
