package com.be.ssm.dto.request.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class AccountUpdateRequest {

    @Size(min = 6, max = 100)
    @Schema(example = "newPassword123")
    private String password;

    @Size(max = 150)
    @Schema(example = "Nguyen Van Updated")
    private String fullName;

}
