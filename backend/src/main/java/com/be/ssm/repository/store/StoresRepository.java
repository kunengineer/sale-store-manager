package com.be.ssm.repository.store;

import com.be.ssm.entities.account.Accounts;
import com.be.ssm.entities.identity.Employees;
import com.be.ssm.entities.store.Stores;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoresRepository extends JpaRepository<Stores, Integer> {

    List<Stores> findAllByManager_Account_AccountId(Integer accountId);

    Optional<Stores> findByStoreCode(String storeCode);
}
