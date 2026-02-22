package com.be.ssm.dto.response.sale;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponse {
    private String method;
    private BigDecimal amount;
    private String referenceNo;
    private String status;
    private LocalDateTime paidAt;
}