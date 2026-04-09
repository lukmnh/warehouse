package com.geli.warehouse.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StockMovementEnum {
    STOCK_IN,
    STOCK_OUT,
    INITIAL_STOCK
}
