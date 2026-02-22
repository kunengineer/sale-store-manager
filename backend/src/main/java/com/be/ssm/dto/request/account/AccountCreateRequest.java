package com.be.ssm.dto.request.account;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class AccountCreateRequest {
    private String username;
    private String password;
    private String fullName;
    private String phone;
    private String email;
}
