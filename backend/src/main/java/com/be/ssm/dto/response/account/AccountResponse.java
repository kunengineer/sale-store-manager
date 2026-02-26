package com.be.ssm.dto.response.account;

import com.be.ssm.enums.account.AccountRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountResponse {
    private Integer accountId;
    private String fullName;
    private String userName;
    private String createdAt;
    private AccountRole role;
}
