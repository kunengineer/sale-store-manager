package com.be.ssm.dto.filter;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductFilter {
    private Integer categoryId;
    private Boolean isActive;
    private String keyword; // tìm theo productName hoặc productCode
    private BigDecimal minBasePrice;
    private BigDecimal maxBasePrice;
    private BigDecimal minCostPrice;
    private BigDecimal maxCostPrice;
}
