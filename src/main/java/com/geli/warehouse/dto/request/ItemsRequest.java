package com.geli.warehouse.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemsRequest {
    @NotBlank(message = "Product name cannot be empty")
    @Size(min = 2, max = 255, message = "Product Name must between 2-255 char")
    private String name;
    private String description;
    private UUID categoryId;
    private UUID brandId;
    @Positive(message = "Price should be more than 0")
    @DecimalMin(value = "500", message = "Minimum price is 500")
    private BigDecimal price;
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer initialStock;
    private Integer stockChange;
    private String warehouseZone;
    private String rackNumber;
}
