package com.geli.warehouse.service;

import com.geli.warehouse.dto.response.CategoriesResponse;

import java.util.List;

public interface CategoriesService {
    List<CategoriesResponse> getAll();
}
