package com.geli.warehouse.dto.request;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariantRequest {
    @NotBlank(message = "Variant name cannot be empty")
    @Size(min = 2, max = 100, message = "Variant name must between 2-100 char")
    private String variantName;
    @NotBlank(message = "Variant value cannot be empty")
    @Size(min = 2, max = 100, message = "Variant value must between 2-100 char")
    private String variantValue;
    @Positive(message = "Price should be more than 0")
    @DecimalMin(value = "500", message = "Minimum price is 500")
    private BigDecimal price;
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer initialStock;
    private Integer stockChange;
    private String warehouseZone;
    private String rackNumber;
}
