package com.be.ssm.dto.response.sale;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class InvoiceResponse {
    private Integer invoiceId;
    private String invoiceNumber;
    private Long orderId;
    private String invoiceType;
    private String buyerName;
    private String buyerTaxCode;
    private String buyerAddress;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private LocalDateTime issuedAt;
    private String pdfUrl;
}
