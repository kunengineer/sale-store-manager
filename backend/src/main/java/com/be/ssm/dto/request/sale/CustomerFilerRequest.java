package com.be.ssm.dto.request.sale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFilerRequest {
    private String customerCode;
    private String fullName;
    private Integer storeId;
}
