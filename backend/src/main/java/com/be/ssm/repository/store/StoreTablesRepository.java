package com.be.ssm.repository.store;

import com.be.ssm.entities.store.StoreTables;
import com.be.ssm.enums.store.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreTablesRepository extends JpaRepository<StoreTables, Integer>, JpaSpecificationExecutor<StoreTables> {

    @Query(value = """
    SELECT EXISTS(
        SELECT 1
        FROM store_tables st
        WHERE st.zone_id = :zoneId
          AND st.table_code LIKE CONCAT('%', :tableCode, '%')
    )
""", nativeQuery = true)
    Long existsByZoneZoneIdAndTableCode(
            @Param("zoneId") Integer zoneId,
            @Param("tableCode") String tableCode
    );

    @Query(nativeQuery = true, value = """
SELECT EXISTS(
    SELECT 1
    FROM store_tables st
    WHERE st.zone_id = :zoneId
      AND st.table_code LIKE CONCAT('%', :tableCode, '%')
        AND st.table_id != :tableId
)
""")
    Long existsTableCodeForUpdate(@Param("tableId") Integer tableId,
                                     @Param("zoneId") Integer zoneId,
                                     @Param("tableCode") String tableCode);

    Optional<StoreTables> findStoreTablesByTableIdAndStatusEquals(Integer tableId, TableStatus status);

    List<StoreTables> findByZoneStoreStoreId(Integer storeId);

    List<StoreTables> findByZoneStoreStoreIdAndStatus(Integer storeId, TableStatus status);
}
