package com.be.ssm.dto.request.identity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PermissionUpdateRequest {
    private String module;
    private String description;
}
