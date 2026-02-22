package com.be.ssm.dto.request.revenue;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RevenueSnapshotUpdateRequest {
    private Integer totalOrders;
    private BigDecimal grossRevenue;
    private BigDecimal discountTotal;
    private BigDecimal netRevenue;
    private BigDecimal cogs;
    private BigDecimal grossProfit;
    private BigDecimal taxCollected;
    private BigDecimal cashSales;
    private BigDecimal cardSales;
    private BigDecimal onlineSales;
}
