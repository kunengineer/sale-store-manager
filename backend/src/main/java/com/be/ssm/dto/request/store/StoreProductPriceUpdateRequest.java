package com.be.ssm.dto.request.store;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreProductPriceUpdateRequest {
    private BigDecimal price;
    private Boolean isActive;
}
