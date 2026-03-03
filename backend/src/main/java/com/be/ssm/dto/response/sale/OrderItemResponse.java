package com.be.ssm.dto.response.sale;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemResponse {
    private Integer orderItemId;
    private Integer orderId;
    private Integer variantId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal discountPct;
    private BigDecimal discountAmt;
    private BigDecimal lineTotal;
    private String note;
}
