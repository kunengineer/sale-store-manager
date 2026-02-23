package com.be.ssm.repository.store;

import com.be.ssm.entities.account.Accounts;
import com.be.ssm.entities.store.Stores;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoresRepository extends JpaRepository<Stores, Integer> {

    Stores findStoresByManager(Accounts manager);
}
