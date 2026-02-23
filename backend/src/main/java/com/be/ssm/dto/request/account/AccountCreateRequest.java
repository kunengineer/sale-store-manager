package com.be.ssm.dto.request.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class AccountCreateRequest {
    @NotBlank(message = "Username must not be blank")
    @Size(max = 50, message = "Username must not exceed 50 characters")
    @Schema(example = "admin01")
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    @Schema(example = "123456")
    private String password;

    @NotBlank(message = "Full name must not be blank")
    @Size(max = 150, message = "Full name must not exceed 150 characters")
    @Schema(example = "Nguyen Van Admin")
    private String fullName;

    @Pattern(regexp = "^(0|\\+84)[0-9]{9,10}$", message = "Phone number is invalid")
    @Schema(example = "0912345678")
    private String phone;

    @Email(message = "Email is invalid")
    @Size(max = 100)
    @Schema(example = "admin@gmail.com")
    private String email;
}
