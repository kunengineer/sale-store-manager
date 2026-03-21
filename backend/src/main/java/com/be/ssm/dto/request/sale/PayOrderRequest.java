package com.be.ssm.dto.request.sale;

import com.be.ssm.enums.sales.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PayOrderRequest {
    private Integer orderId;
    private String note;
    private String buyerName;
    private BigDecimal discountAmount;
    private BigDecimal surcharge;
    private PaymentStatus paymentStatus;
    private BigDecimal amountGiveByGuest;
    private BigDecimal change;


}
