package com.geli.warehouse.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "items", schema = "geli")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class Items extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "product_code", unique = true, nullable = false)
    private String productCode;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Categories category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brands brand;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "stock")
    @Builder.Default
    private Integer stock = 0;

    @Column(name = "warehouse_zone")
    private String warehouseZone;

    @Column(name = "rack_number")
    private String rackNumber;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Variant> variants = new ArrayList<>();

    public boolean hasEnoughStock(int requestedQuantity) {
        return this.stock >= requestedQuantity;
    }

    public void reduceStock(int quantity) {
        if (!hasEnoughStock(quantity)) {
            throw new IllegalStateException(
                    String.format("Stock insufficient! Product: %s, Stock: %d, Requested: %d",
                            this.productCode, this.stock, quantity)
            );
        }
        this.stock -= quantity;
    }


    public String getCategoryName() {
        return category != null ? category.getName() : null;
    }

    public String getBrandName() {
        return brand != null ? brand.getName() : null;
    }
}
