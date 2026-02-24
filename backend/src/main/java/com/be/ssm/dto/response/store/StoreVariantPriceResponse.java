package com.be.ssm.dto.response.store;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreVariantPriceResponse {
    private Integer storeVariantPriceId;
    private Integer storeId;
    private Integer variantId;
    private BigDecimal price;
    private Boolean isActive;
}
