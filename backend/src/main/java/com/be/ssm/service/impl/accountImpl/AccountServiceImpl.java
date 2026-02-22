package com.be.ssm.service.impl.accountImpl;


import com.be.ssm.dto.request.account.AccountCreateRequest;
import com.be.ssm.dto.request.account.AccountUpdateRequest;
import com.be.ssm.dto.response.account.AccountResponse;
import com.be.ssm.entities.account.Accounts;
import com.be.ssm.mapper.account.AccountMapper;
import com.be.ssm.repository.account.AccountRepository;
import com.be.ssm.service.account.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

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

        return mapper.toAccountResponse(repository.save(account));
    }

    @Override
    public AccountResponse update(AccountUpdateRequest request, Integer id) {
        log.info("Update account with id {}", id);

        Accounts account = findById(id);

        mapper.updateEntityFromRequest(request, account);

        return mapper.toAccountResponse(repository.save(account));
    }

    private Accounts findById(Integer id) {
        log.info("Finding account by id {}", id);

        return repository.findById(id)
                .orElseThrow();
    }
}