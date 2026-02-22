package com.be.ssm.dto.response.sale;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CustomerResponse {
    private Integer customerId;
    private String customerCode;
    private String fullName;
    private String phone;
    private String gender;
    private Integer loyaltyPoints;
    private BigDecimal totalSpent;
    private String note;
    private LocalDateTime createdAt;
}
