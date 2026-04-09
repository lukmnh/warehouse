package com.geli.warehouse.service;

import com.geli.warehouse.dto.request.ItemsRequest;
import com.geli.warehouse.dto.request.StockReduceRequest;
import com.geli.warehouse.dto.response.ItemsResponse;

import java.util.List;
import java.util.UUID;

public interface ItemsService {
    List<ItemsResponse> getAll();
    ItemsResponse getById(UUID id);
    ItemsResponse create(ItemsRequest request);
    ItemsResponse update(UUID id, ItemsRequest request);
    void delete(UUID id);
    ItemsResponse reduceStock(UUID id, StockReduceRequest request);
}
