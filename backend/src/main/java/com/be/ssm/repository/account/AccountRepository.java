package com.be.ssm.repository.account;

import com.be.ssm.entities.account.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Accounts, Integer> {
    Optional<Accounts> findByUsername(String username);

    boolean existsByUsername(String username);
}
