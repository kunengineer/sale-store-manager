package com.be.ssm.dto.request.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductUpdateRequest {
    private String productCode;
    private String productName;
    private String description;
    private String unit;
    private String images;
    private Boolean isActive;
}
