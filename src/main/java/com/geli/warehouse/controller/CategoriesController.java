package com.geli.warehouse.controller;

import com.geli.warehouse.dto.response.CategoriesResponse;
import com.geli.warehouse.service.CategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/api/categories")
@RequiredArgsConstructor
public class CategoriesController {
    private final CategoriesService categoriesService;

    @GetMapping
    public ResponseEntity<List<CategoriesResponse>> getAll(){
        return ResponseEntity.ok(categoriesService.getAll());
    }
}
