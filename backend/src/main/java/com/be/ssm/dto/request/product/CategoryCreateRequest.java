package com.be.ssm.dto.request.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryCreateRequest {
    private String categoryName;
    private Integer sortOrder;
    private Boolean isActive;
    private String imageUrl;
}
