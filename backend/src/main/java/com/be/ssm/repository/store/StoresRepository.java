package com.be.ssm.repository.store;

import com.be.ssm.entities.account.Accounts;
import com.be.ssm.entities.store.Stores;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoresRepository extends JpaRepository<Stores, Integer> {

    List<Stores> findAllByManager(Accounts manager);

    Optional<Stores> findByStoreCode(String storeCode);
}
