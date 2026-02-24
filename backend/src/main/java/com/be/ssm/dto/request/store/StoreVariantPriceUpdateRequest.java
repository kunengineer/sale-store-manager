package com.be.ssm.dto.request.store;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreVariantPriceUpdateRequest {
    private BigDecimal price;
    private Boolean isActive;
}
