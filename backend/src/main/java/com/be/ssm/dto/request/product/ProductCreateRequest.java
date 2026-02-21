package com.be.ssm.dto.request.product;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductCreateRequest {
    private String productCode;
    private String productName;
    private String description;
    private String unit;
    private BigDecimal basePrice;
    private BigDecimal costPrice;
    private String images;
    private Boolean isActive;
}
