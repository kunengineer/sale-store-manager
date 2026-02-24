package com.be.ssm.service.impl.accountImpl;


import com.be.ssm.dto.request.account.AccountCreateRequest;
import com.be.ssm.dto.request.account.AccountUpdateRequest;
import com.be.ssm.dto.request.account.FormLogin;
import com.be.ssm.dto.response.account.AccountResponse;
import com.be.ssm.dto.response.account.AuthenticationResponse;
import com.be.ssm.entities.account.Accounts;
import com.be.ssm.exceptions.CustomException;
import com.be.ssm.exceptions.Error;
import com.be.ssm.mapper.account.AccountMapper;
import com.be.ssm.repository.account.AccountRepository;
import com.be.ssm.service.account.AccountService;
import com.be.ssm.utils.JwtTokenUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    private final AccountRepository repository;
    private final AccountMapper mapper;

    @Override
    public AccountResponse getById(Integer id) {
        log.info("Getting account by id {}", id);

        return mapper.toAccountResponse(findById(id));
    }

    @Override
    public AccountResponse create(AccountCreateRequest request) {
        log.info("Create new account");

        Accounts account = mapper.toAccountEntity(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        return mapper.toAccountResponse(repository.save(account));
    }

    @Override
    public AccountResponse update(AccountUpdateRequest request, Integer id) {
        log.info("Update account with id {}", id);

        Accounts account = findById(id);

        mapper.updateEntityFromRequest(request, account);

        return mapper.toAccountResponse(repository.save(account));
    }

    @Override
    public AuthenticationResponse signIn(FormLogin formLogin) {
        log.info("Sign in account");
        Accounts account = repository.findByUsername(formLogin.getUsername())
                .orElseThrow(()-> new CustomException(Error.ACCOUNT_NOT_FOUND));

        if (!account.isAccountNonLocked()) {
            throw new CustomException(Error.ACCOUNT_LOCKED);
        }

        if (!passwordEncoder.matches(formLogin.getPassword(), account.getPassword())) {
            throw new CustomException(Error.ACCOUNT_INVALID_PASSWORD);
        }

        try {
            String jwtToken = jwtTokenUtil.generateToken((UserDetails) account);
            String refreshToken = jwtTokenUtil.generateRefreshToken((UserDetails) account);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .refreshToken(refreshToken)
                    .role(account.getRole().name())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Accounts findById(Integer id) {
        log.info("Finding account by id {}", id);

        return repository.findById(id)
                .orElseThrow(()-> new CustomException(Error.ACCOUNT_NOT_FOUND));
    }
}