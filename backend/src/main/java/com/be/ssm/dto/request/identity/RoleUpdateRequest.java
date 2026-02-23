package com.be.ssm.dto.request.identity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleUpdateRequest {
    @Size(max = 50)
    @Schema(example = "Cashier")
    private String roleName;

    @Schema(example = "Cashier role for POS operations")
    private String description;

    @Schema(example = "false")
    private Boolean isSystem;
}