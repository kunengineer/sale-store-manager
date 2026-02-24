package com.be.ssm.dto.response.store;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreProductPriceResponse {
    private Integer id;
    private Integer storeId;
    private String storeName;

    private Integer productId;
    private String productName;

    private BigDecimal price;
    private Boolean isActive;
}
