package com.be.ssm.dto.response.product;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductVariantResponse {
    private Integer variantId;
    private Integer productId;
    private String variantName;
    private String sku;
    private String barcode;
    private String attributes;
    private BigDecimal price;
    private BigDecimal costPrice;
    private Integer weightGram;
    private Boolean isActive;
}
