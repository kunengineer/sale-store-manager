package com.be.ssm.dto.response.identity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PermissionResponse {
    private Integer permId;
    private String permCode;
    private String module;
    private String description;
}