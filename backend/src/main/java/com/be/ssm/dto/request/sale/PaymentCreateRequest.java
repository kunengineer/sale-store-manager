package com.be.ssm.dto.request.sale;

import com.be.ssm.enums.sales.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import com.be.ssm.enums.sales.PaymentMethod;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentCreateRequest {
    private Integer invoiceId;
    private PaymentMethod method;
    private PaymentStatus status;
    private BigDecimal amount;
    private BigDecimal amountTendered;
    private BigDecimal changeAmount;
}
