package com.geli.warehouse.controller;

import com.geli.warehouse.dto.response.StockResponse;
import com.geli.warehouse.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/v1/api/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<StockResponse>> getByItem(@PathVariable UUID itemId) {
        return ResponseEntity.ok(stockService.getByItem(itemId));
    }

    @GetMapping("/variant/{variantId}")
    public ResponseEntity<List<StockResponse>> getByVariant(@PathVariable UUID variantId) {
        return ResponseEntity.ok(stockService.getByVariant(variantId));
    }
}
