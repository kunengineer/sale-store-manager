package com.be.ssm.dto.request.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductCreateRequest {
    private Integer storeId;
    private Integer categoryId;
    private String productCode;
    private String productName;
    private String description;
    private String unit;
    private BigDecimal basePrice;
    private BigDecimal costPrice;
    private String images;
    private Boolean isActive;
}
