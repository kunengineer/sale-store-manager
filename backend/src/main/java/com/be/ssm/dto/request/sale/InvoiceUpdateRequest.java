package com.be.ssm.dto.request.sale;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class InvoiceUpdateRequest {
    private String invoiceType;
    private String buyerName;
    private String buyerTaxCode;
    private String buyerAddress;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private String pdfUrl;
}
