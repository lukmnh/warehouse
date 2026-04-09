package com.geli.warehouse.service.impl;

import com.geli.warehouse.dto.response.CategoriesResponse;
import com.geli.warehouse.model.Categories;
import com.geli.warehouse.repository.CategoriesRepository;
import com.geli.warehouse.service.CategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {

    private  final CategoriesRepository categoriesRepository;
    @Override
    public List<CategoriesResponse> getAll() {
        return categoriesRepository.findAll().stream()
                .map(this::setTheResponse)
                .collect(Collectors.toList());
    }

    private CategoriesResponse setTheResponse(Categories categories) {
        return CategoriesResponse.builder()
                .id(categories.getId())
                .code(categories.getCode())
                .name(categories.getName())
                .description(categories.getDescription())
                .createdAt(categories.getCreatedAt())
                .updatedAt(categories.getUpdatedAt())
                .build();
    }
}
