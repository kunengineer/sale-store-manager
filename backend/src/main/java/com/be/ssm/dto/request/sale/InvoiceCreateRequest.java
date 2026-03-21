package com.be.ssm.dto.request.sale;

import com.be.ssm.enums.sales.InvoiceStatus;
import com.be.ssm.enums.sales.InvoiceType;
import com.be.ssm.enums.sales.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class InvoiceCreateRequest {
    @Schema(example = "RETAIL")
    private InvoiceType invoiceType;

    private String buyerName;

    @NotNull(message = "Order id must not be null")
    @Positive(message = "Order id must be positive")
    @Schema(example = "10")
    private Integer orderId;
    private BigDecimal vat;
    private InvoiceStatus status;

    private String note;
    private PaymentCreateRequest paymentCreateRequest;
}
