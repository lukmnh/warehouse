package com.geli.warehouse.dto.response;

import com.geli.warehouse.constant.StockMovementEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockResponse {
    private UUID id;
    private UUID itemId;
    private String itemName;
    private String itemCode;
    private UUID variantId;
    private String variantName;
    private String variantValue;
    private StockMovementEnum stockType;
    private Integer quantityBefore;
    private Integer quantityChange;
    private Integer quantityAfter;
    private String notes;
    private LocalDateTime createdAt;
}
