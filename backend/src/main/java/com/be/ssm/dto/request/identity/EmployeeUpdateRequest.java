package com.be.ssm.dto.request.identity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class EmployeeUpdateRequest {
    private Integer roleId;
    private String fullName;
    private String phone;
    private String email;
    private LocalDateTime dob;
    private String salaryType;
    private BigDecimal baseSalary;
    private String idNumber;
    private Boolean isActive;
}