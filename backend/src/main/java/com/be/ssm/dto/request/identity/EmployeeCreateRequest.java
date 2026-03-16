package com.be.ssm.dto.request.identity;

import com.be.ssm.dto.request.account.AccountCreateRequest;
import com.be.ssm.enums.identity.SalaryType;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class EmployeeCreateRequest {
    private String empCode;
    private String fullName;
    private String phone;
    private String email;
    private LocalDateTime dob;
    private LocalDateTime hireDate;
    private SalaryType salaryType;
    private BigDecimal baseSalary;
    private String idNumber;
    private Boolean isActive;
    private Integer storeId;
    private Integer roleId;
    private Integer workShiftId;
    private AccountCreateRequest accountCreateRequest;
}
