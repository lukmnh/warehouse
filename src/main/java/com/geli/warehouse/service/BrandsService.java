package com.geli.warehouse.service;

import com.geli.warehouse.dto.response.BrandsResponse;

import java.util.List;

public interface BrandsService {
    List<BrandsResponse> getAll();
}
