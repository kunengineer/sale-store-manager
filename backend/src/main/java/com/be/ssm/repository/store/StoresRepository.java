package com.be.ssm.repository.store;

import com.be.ssm.entities.store.Stores;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoresRepository extends JpaRepository<Stores, Integer> {
}
