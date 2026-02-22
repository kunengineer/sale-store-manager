package com.be.ssm.dto.response.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoriesResponse {
    private Integer categoryId;
    private Long parentId;
    private String categoryName;
    private Integer sortOrder;
    private Boolean isActive;
    private String imageUrl;
}
