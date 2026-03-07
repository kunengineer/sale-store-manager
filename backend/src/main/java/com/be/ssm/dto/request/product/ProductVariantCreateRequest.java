package com.be.ssm.dto.request.product;

import com.be.ssm.enums.product.VariantType;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Builder
@Data
public class ProductVariantCreateRequest {
    private Integer productId;
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
