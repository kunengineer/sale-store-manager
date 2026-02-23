package com.be.ssm.repository.store;

import com.be.ssm.entities.store.StoreZones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StoreZonesRepository extends JpaRepository<StoreZones, Integer>, JpaSpecificationExecutor<StoreZones> {
}
