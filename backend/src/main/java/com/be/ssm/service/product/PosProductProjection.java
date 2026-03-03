package com.be.ssm.service.product;

import com.be.ssm.enums.product.VariantType;

import java.math.BigDecimal;

public interface PosProductProjection {
    Integer getProductId();
    String getProductName();
    String getCategoryName();
    Integer getVariantId();
    String getVariantName();
    BigDecimal getFinalPrice();
    VariantType getVariantType();
}
