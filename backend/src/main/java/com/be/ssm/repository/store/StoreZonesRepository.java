package com.be.ssm.repository.store;

import com.be.ssm.entities.store.StoreZones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface StoreZonesRepository extends JpaRepository<StoreZones, Integer>, JpaSpecificationExecutor<StoreZones> {
    List<StoreZones> findByStoreStoreId(Integer storeId);
}
