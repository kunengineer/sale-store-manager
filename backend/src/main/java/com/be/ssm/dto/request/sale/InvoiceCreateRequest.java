package com.be.ssm.dto.request.sale;

import com.be.ssm.enums.sales.InvoiceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class InvoiceCreateRequest {
    @NotNull(message = "Invoice type must not be null")
    @Schema(example = "RETAIL")
    private InvoiceType invoiceType;

    @Size(max = 150, message = "Buyer name must not exceed 150 characters")
    @Schema(example = "Nguyen Van A")
    private String buyerName;

    @NotNull(message = "Subtotal must not be null")
    @DecimalMin(value = "0.00", inclusive = true, message = "Subtotal must be >= 0")
    @Digits(integer = 13, fraction = 2)
    @Schema(example = "500000.00")
    private BigDecimal subtotal;

    @NotNull(message = "Tax amount must not be null")
    @DecimalMin(value = "0.00", inclusive = true)
    @Digits(integer = 13, fraction = 2)
    @Schema(example = "50000.00")
    private BigDecimal taxAmount;

    @NotNull(message = "Total amount must not be null")
    @DecimalMin(value = "0.00", inclusive = true)
    @Digits(integer = 13, fraction = 2)
    @Schema(example = "550000.00")
    private BigDecimal totalAmount;

    @Size(max = 255, message = "PDF URL must not exceed 255 characters")
    @Schema(example = "https://cdn.example.com/invoices/INV0001.pdf")
    private String pdfUrl;

    @NotNull(message = "Order id must not be null")
    @Positive(message = "Order id must be positive")
    @Schema(example = "10")
    private Integer orderId;
}
