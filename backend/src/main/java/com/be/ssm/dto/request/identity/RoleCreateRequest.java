package com.be.ssm.dto.request.identity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.*;

@Data
@Builder
public class RoleCreateRequest {
    @NotBlank(message = "Role name must not be blank")
    @Size(max = 50, message = "Role name must not exceed 50 characters")
    @Schema(example = "Manager")
    private String roleName;

    @Schema(example = "Store manager with full permissions")
    private String description;

    @NotNull(message = "System flag must not be null")
    @Schema(example = "true")
    private Boolean isSystem;
}
