package com.be.ssm.dto.request.sale;

import com.be.ssm.enums.sales.PaymentMethod;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentCreateRequest {
    private PaymentMethod method;
    private BigDecimal amount;
    private String referenceNo;
    private Integer invoiceId;
}
