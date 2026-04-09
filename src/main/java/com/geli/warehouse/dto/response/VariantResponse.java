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
public class VariantResponse {
    private UUID id;
    private UUID itemId;
    private String itemName;
    private String itemCode;
    private String variantName;
    private String variantValue;
    private BigDecimal price;
    private Integer stock;
    private String warehouseZone;
    private String rackNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
