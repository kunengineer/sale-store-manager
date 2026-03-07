package com.be.ssm.dto.filter;

import lombok.Data;

@Data
public class CategoryFilter {
    private Integer storeId;
    private Integer parentId;
    private String categoryName;
    private Boolean isActive;
}