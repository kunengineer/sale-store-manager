package com.be.ssm.repository.identity;

import com.be.ssm.entities.account.Accounts;
import com.be.ssm.entities.identity.Employees;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeesRepository extends JpaRepository<Employees, Integer> {
    Optional<Employees> findByAccount(Accounts account);

    Optional<Employees> findEmployeesByAccount_AccountId(Integer accountId);
}
