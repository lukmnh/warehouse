package com.geli.warehouse.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemsResponse {
    private UUID id;
    private String productCode;
    private String name;
    private String description;
    private UUID categoryId;
    private String categoryName;
    private UUID brandId;
    private String brandName;
    private BigDecimal price;
    private Integer stock;
    private String warehouseZone;
    private String rackNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
