package com.be.ssm.dto.request.sale;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentUpdateRequest {
    private String status;
    private String referenceNo;
}
