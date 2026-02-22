package com.be.ssm.dto.request.sale;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemCreateRequest {
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal discountPct;
    private BigDecimal discountAmt;
    private BigDecimal lineTotal;
    private String note;
    private Integer orderId;
    private Integer productId;
}
