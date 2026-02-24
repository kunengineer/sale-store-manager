package com.be.ssm.service.account;

import com.be.ssm.dto.request.account.AccountCreateRequest;
import com.be.ssm.dto.request.account.AccountUpdateRequest;
import com.be.ssm.dto.request.account.FormLogin;
import com.be.ssm.dto.response.account.AccountResponse;
import com.be.ssm.dto.response.account.AuthenticationResponse;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    AccountResponse getById(Integer id);

    AccountResponse create(AccountCreateRequest request);

    AccountResponse update(AccountUpdateRequest request, Integer accountId);

    AuthenticationResponse signIn(FormLogin formLogin);
}
