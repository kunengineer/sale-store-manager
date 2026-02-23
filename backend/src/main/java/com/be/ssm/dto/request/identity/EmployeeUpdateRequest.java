package com.be.ssm.dto.request.identity;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class EmployeeUpdateRequest {
    private Integer roleId;

    @Size(max = 150)
    private String fullName;

    @Pattern(regexp = "^(0|\\+84)[0-9]{9,10}$")
    private String phone;

    @Email
    private String email;

    private LocalDateTime dob;
    private String salaryType;

    @DecimalMin("0.0")
    private BigDecimal baseSalary;

    private String idNumber;
    private Boolean isActive;
    private Integer workShiftId;
}