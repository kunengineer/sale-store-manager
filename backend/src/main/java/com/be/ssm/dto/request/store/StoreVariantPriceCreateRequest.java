package com.be.ssm.dto.request.store;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreVariantPriceCreateRequest {
    private Integer storeId;
    private Integer variantId;
    private BigDecimal price;
}
