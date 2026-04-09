package com.geli.warehouse.repository;

import com.geli.warehouse.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {
    List<Stock> findByItemIdOrderByCreatedAtDesc(UUID itemId);
    List<Stock> findByVariantIdOrderByCreatedAtDesc(UUID variantId);
}

