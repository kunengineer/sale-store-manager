package com.be.ssm.dto.request.product;

import lombok.Data;

@Data
public class ProductVariantUpdateRequest {
    private String variantName;
    private String sku;
    private String barcode;
    private String attributes;
    private String price;
    private String costPrice;
    private Integer weightGram;
    private Boolean isActive;
}
