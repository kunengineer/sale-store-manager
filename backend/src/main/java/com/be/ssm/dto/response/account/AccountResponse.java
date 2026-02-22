package com.be.ssm.dto.response.account;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountResponse {
    private Integer accountId;
    private String fullName;
    private String phone;
    private String email;
}
