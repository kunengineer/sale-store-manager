package com.be.ssm.dto.request.product;

import com.be.ssm.enums.product.VariantType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVariantUpdateRequest {
    private String variantName;
    private VariantType variantType;
    private String sku;
    private String barcode;
    private String attributes;
    private BigDecimal price;
    private BigDecimal costPrice;
    private Integer weightGram;
    private Boolean isActive;
}
