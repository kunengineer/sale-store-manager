package com.be.ssm.dto.response.identity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleResponse {
    private Integer roleId;
    private String roleName;
    private String description;
    private Boolean isSystem;
}
