package com.geli.warehouse.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "variants", schema = "geli")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class Variant extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Items item;

    @Column(name = "variant_name", nullable = false)
    private String variantName;

    @Column(name = "variant_value", nullable = false)
    private String variantValue;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "stock")
    @Builder.Default
    private Integer stock = 0;

    @Column(name = "warehouse_zone")
    private String warehouseZone;

    @Column(name = "rack_number")
    private String rackNumber;

    public BigDecimal getEffectivePrice() {
        return price != null ? price : item.getPrice();
    }

    public boolean hasEnoughStock(int requestedQuantity) {
        return this.stock >= requestedQuantity;
    }

    public void reduceStock(int quantity) {
        if (!hasEnoughStock(quantity)) {
            throw new IllegalStateException(
                    String.format("Variant stock insufficient! Variant: %s-%s, Stock: %d, Requested: %d",
                            variantName, variantValue, this.stock, quantity)
            );
        }
        this.stock -= quantity;
    }

    public void addStock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.stock += quantity;
    }
}
