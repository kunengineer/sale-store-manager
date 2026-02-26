package com.be.ssm.dto.request.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FormLogin {
    @Schema(example = "admin01")
    private String username;

    @Schema(example = "password123")
    private String password;
}
