package com.be.ssm.repository.account;

import com.be.ssm.entities.account.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Accounts, Integer> {
}
