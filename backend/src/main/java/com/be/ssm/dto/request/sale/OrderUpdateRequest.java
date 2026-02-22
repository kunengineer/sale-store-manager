package com.be.ssm.dto.request.sale;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderUpdateRequest {
    private String status;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal vat;
    private BigDecimal taxAmount;
    private BigDecimal grandTotal;
    private String note;
}
