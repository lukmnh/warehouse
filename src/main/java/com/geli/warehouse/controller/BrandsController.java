package com.geli.warehouse.controller;

import com.geli.warehouse.dto.response.BrandsResponse;
import com.geli.warehouse.dto.response.ItemsResponse;
import com.geli.warehouse.service.BrandsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/api/brands")
@RequiredArgsConstructor
public class BrandsController {

    private final BrandsService brandsService;

    @GetMapping
    public ResponseEntity<List<BrandsResponse>> getAll() {
        return ResponseEntity.ok(brandsService.getAll());
    }
}
