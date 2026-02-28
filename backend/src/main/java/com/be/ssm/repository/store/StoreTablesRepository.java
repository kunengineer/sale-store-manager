package com.be.ssm.repository.store;

import com.be.ssm.entities.store.StoreTables;
import com.be.ssm.enums.store.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface StoreTablesRepository extends JpaRepository<StoreTables, Integer>, JpaSpecificationExecutor<StoreTables> {
    boolean existsByZoneZoneIdAndTableCode(Integer zoneId, String tableCode);

    List<StoreTables> findByZoneZoneId(Integer zoneId);

    List<StoreTables> findByZoneStoreStoreId(Integer storeId);

    List<StoreTables> findByZoneStoreStoreIdAndStatus(Integer storeId, TableStatus status);
}
