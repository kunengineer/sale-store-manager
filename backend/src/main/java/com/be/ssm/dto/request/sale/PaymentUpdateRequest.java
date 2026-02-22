package com.be.ssm.dto.request.sale;

import com.be.ssm.enums.sales.PaymentMethod;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentUpdateRequest {
    private PaymentMethod status;
    private String referenceNo;
    private Integer invoiceId;
}
