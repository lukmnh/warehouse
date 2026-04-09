package com.geli.warehouse.controller;

import com.geli.warehouse.dto.request.ItemsRequest;
import com.geli.warehouse.dto.request.StockReduceRequest;
import com.geli.warehouse.dto.response.ItemsResponse;
import com.geli.warehouse.service.ItemsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/items")
@RequiredArgsConstructor
public class ItemsController {
    
    private final ItemsService itemsService;
    
    @GetMapping
    public ResponseEntity<List<ItemsResponse>> getAll() {
        return ResponseEntity.ok(itemsService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemsResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(itemsService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ItemsResponse> create(@Valid @RequestBody ItemsRequest request) {
        ItemsResponse created = itemsService.create(request);
        return ResponseEntity.created(URI.create("/api/items/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemsResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody ItemsRequest request) {
        return ResponseEntity.ok(itemsService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        itemsService.delete(id);
    }

    @PatchMapping("/{id}/reduce-stock")
    public ResponseEntity<ItemsResponse> reduceStock(
            @PathVariable UUID id,
            @Valid @RequestBody StockReduceRequest request) {
        return ResponseEntity.ok(itemsService.reduceStock(id, request));
    }
}
