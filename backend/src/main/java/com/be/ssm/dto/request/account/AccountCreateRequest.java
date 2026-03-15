package com.be.ssm.dto.request.account;

import com.be.ssm.enums.account.AccountRole;
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

    @Size(max = 50)
    @Schema(example = "admin01")
    private String username;

    @Size(min = 6, max = 100)
    @Schema(example = "password123")
    private String password;

    @Schema(
            description = "Account email",
            example = "admin@gmail.com"
    )
    private String email;

    @Schema(example = "ADMIN")
    private AccountRole role;
}