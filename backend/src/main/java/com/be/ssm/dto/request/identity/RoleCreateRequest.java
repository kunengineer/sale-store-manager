package com.be.ssm.dto.request.identity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleCreateRequest {
    private String roleName;
    private String description;
    private Boolean isSystem;
}
