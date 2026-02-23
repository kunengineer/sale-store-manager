package com.be.ssm.repository.store;

import com.be.ssm.entities.store.StoreTables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StoreTablesRepository extends JpaRepository<StoreTables, Integer>, JpaSpecificationExecutor<StoreTables> {
    boolean existsByZoneZoneIdAndTableCode(Integer zoneId, String tableCode);
}
