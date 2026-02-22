package com.be.ssm.dto.request.sale;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerCreateRequest {
    private String fullName;
    private String phone;
    private String gender;
    private String note;
}
