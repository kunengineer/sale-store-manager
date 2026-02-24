package com.be.ssm.repository.store;

import com.be.ssm.entities.store.StoreVariantPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StoreVariantPriceRepository extends JpaRepository<StoreVariantPrice, Integer> {
    @Query("""
        SELECT svp FROM StoreVariantPrice svp
        WHERE svp.store.storeId = :storeId
        AND svp.productVariant.variantId = :variantId
        AND svp.isActive = true
    """)
    Optional<StoreVariantPrice> findActivePrice(
            @Param("storeId") Integer storeId,
            @Param("variantId") Integer variantId
    );
}
