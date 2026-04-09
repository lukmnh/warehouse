package com.geli.warehouse.service.impl;

import com.geli.warehouse.dto.response.BrandsResponse;
import com.geli.warehouse.model.Brands;
import com.geli.warehouse.repository.BrandsRepository;
import com.geli.warehouse.service.BrandsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandsServiceImpl implements BrandsService {

    private final BrandsRepository brandsRepository;

    @Override
    public List<BrandsResponse> getAll() {
        return brandsRepository.findAll().stream()
                .map(this::setTheResponse)
                .collect(Collectors.toList());
    }

    private BrandsResponse setTheResponse(Brands brands) {
        return BrandsResponse.builder()
                .id(brands.getId())
                .code(brands.getCode())
                .name(brands.getName())
                .description(brands.getDescription())
                .createdAt(brands.getCreatedAt())
                .updatedAt(brands.getUpdatedAt())
                .build();
    }
}
