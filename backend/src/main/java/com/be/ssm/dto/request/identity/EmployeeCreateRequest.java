package com.be.ssm.dto.request.identity;

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
    @NotBlank
    @Size(max = 20)
    @Schema(example = "EMP001")
    private String empCode;

    @NotBlank
    @Size(max = 150)
    @Schema(example = "Nguyen Van A")
    private String fullName;

    @Pattern(regexp = "^(0|\\+84)[0-9]{9,10}$", message = "Phone number is invalid")
    @Schema(example = "0912345678")
    private String phone;

    @Email
    @Schema(example = "employee@gmail.com")
    private String email;

    @Schema(example = "1999-05-20T00:00:00")
    private LocalDateTime dob;

    @NotNull
    @Schema(example = "2024-01-01T08:00:00")
    private LocalDateTime hireDate;

    @NotBlank
    @Schema(example = "MONTHLY")
    private SalaryType salaryType;

    @NotNull
    @DecimalMin("0.0")
    @Schema(example = "7000000")
    private BigDecimal baseSalary;

    @Schema(example = "012345678901")
    private String idNumber;

    @NotNull
    @Schema(example = "true")
    private Boolean isActive;

    @NotNull
    @Schema(example = "1")
    private Integer storeId;

    @NotNull
    @Schema(example = "2")
    private Integer roleId;

    @NotNull
    @Schema(example = "1")
    private Integer workShiftId;
}
