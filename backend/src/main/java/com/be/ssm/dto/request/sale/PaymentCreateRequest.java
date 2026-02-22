package com.be.ssm.dto.request.sale;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentCreateRequest {
    private String method;
    private BigDecimal amount;
    private String referenceNo;
}
