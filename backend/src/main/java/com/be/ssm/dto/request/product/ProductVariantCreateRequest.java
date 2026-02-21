package com.be.ssm.dto.request.product;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class ProductVariantCreateRequest {
    private String variantName;
    private String sku;
    private String barcode;
    private String attributes;
    private BigDecimal price;
    private BigDecimal costPrice;
    private Integer weightGram;
    private Boolean isActive;
}
