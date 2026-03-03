package com.be.ssm.dto.response.product;

import com.be.ssm.enums.product.VariantType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class PosVariantResponse {
    private Integer variantId;
    private String variantName;
    private BigDecimal price;
    private VariantType variantType;
}
