package com.be.ssm.dto.request.sale;

import com.be.ssm.enums.sales.PaymentMethod;
import com.be.ssm.enums.sales.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentUpdateRequest {
    @NotNull(message = "Invoice id must not be null")
    @Positive(message = "Invoice id must be positive")
    @Schema(example = "5")
    private Integer invoiceId;

    @NotNull(message = "Amount must not be null")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than 0")
    @Digits(integer = 13, fraction = 2)
    @Schema(example = "550000.00")
    private BigDecimal amount;

    @Size(max = 100, message = "Transaction id must not exceed 100 characters")
    @Schema(example = "TXN123456789")
    private String transactionId;

    @Schema(example = "PENDING")
    private PaymentStatus status;
}
