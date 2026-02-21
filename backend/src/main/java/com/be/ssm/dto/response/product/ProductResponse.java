package com.be.ssm.dto.response.product;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductResponse {
    private Integer productId;
    private Long categoryId;
    private String productCode;
    private String productName;
    private String description;
    private String unit;
    private BigDecimal basePrice;
    private BigDecimal costPrice;
    private String images;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
