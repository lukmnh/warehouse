package com.geli.warehouse.controller;

import com.geli.warehouse.dto.request.StockReduceRequest;
import com.geli.warehouse.dto.request.VariantRequest;
import com.geli.warehouse.dto.response.VariantResponse;
import com.geli.warehouse.service.VariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/v1/api/variant")
@RequiredArgsConstructor
public class VariantController {
    private final VariantService variantService;

    @GetMapping
    public ResponseEntity<List<VariantResponse>> getAll() {
        return ResponseEntity.ok(variantService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<VariantResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(variantService.getById(id));
    }


    @PostMapping("/{itemId}")
    public ResponseEntity<VariantResponse> create(
            @PathVariable UUID itemId,
            @Valid @RequestBody VariantRequest request) {
        VariantResponse created = variantService.create(itemId, request);
        return ResponseEntity.created(URI.create("/v1/api/variant/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VariantResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody VariantRequest request) {
        return ResponseEntity.ok(variantService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        variantService.delete(id);
    }

    @PatchMapping("/{id}/reduce-stock")
    public ResponseEntity<VariantResponse> reduceStock(
            @PathVariable UUID id,
            @Valid @RequestBody StockReduceRequest request) {
        return ResponseEntity.ok(variantService.reduceStock(id, request));
    }
}
