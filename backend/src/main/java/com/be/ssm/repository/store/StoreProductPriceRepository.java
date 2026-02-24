package com.be.ssm.repository.store;

import com.be.ssm.entities.store.StoreProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StoreProductPriceRepository extends JpaRepository<StoreProductPrice, Integer> {
    @Query("""
        SELECT spp FROM StoreProductPrice spp
        WHERE spp.store.storeId = :storeId
        AND spp.product.productId = :productId
        AND spp.isActive = true
    """)
    Optional<StoreProductPrice> findActivePrice(
            @Param("storeId") Integer storeId,
            @Param("productId") Integer productId
    );
}
