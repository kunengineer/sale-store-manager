package com.be.ssm.dto.request.sale;

import com.be.ssm.enums.sales.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderCreateRequest {
    private OrderStatus status;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal vat;
    private BigDecimal taxAmount;
    private BigDecimal grandTotal;
    private String note;
    private Integer customerId;
    private Integer tableId;
}
