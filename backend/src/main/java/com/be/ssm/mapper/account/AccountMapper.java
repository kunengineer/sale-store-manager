package com.be.ssm.mapper.account;

import com.be.ssm.dto.request.account.AccountCreateRequest;
import com.be.ssm.dto.request.account.AccountUpdateRequest;
import com.be.ssm.dto.response.account.AccountResponse;
import com.be.ssm.entities.account.Accounts;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountResponse toAccountResponse(Accounts accounts);

    @Mapping(source = "role", target = "role")
    Accounts toAccountEntity(AccountCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(AccountUpdateRequest request, @MappingTarget Accounts entity);
}
