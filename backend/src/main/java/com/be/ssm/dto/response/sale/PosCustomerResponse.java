package com.be.ssm.dto.response.sale;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PosCustomerResponse {
    private Integer customerId;

    private String customerCode;

    private String fullName;

    private String phone;

    private Integer loyaltyPoints;

    private BigDecimal totalSpent;
}
