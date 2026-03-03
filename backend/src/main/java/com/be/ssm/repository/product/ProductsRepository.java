package com.be.ssm.repository.product;

import com.be.ssm.dto.response.product.PosProductResponse;
import com.be.ssm.entities.product.Products;
import com.be.ssm.service.product.PosProductProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductsRepository extends JpaRepository<Products, Integer>, JpaSpecificationExecutor<Products> {
    @Query("""
    SELECT 
        p.productId as productId,
        p.productName as productName,
        c.categoryName as categoryName,
        v.variantId as variantId,
        v.variantName as variantName,
        v.variantType as variantType,
        COALESCE(svp.price, v.price) as finalPrice
    FROM Products p
    JOIN p.category c
    JOIN ProductVariants v ON v.product.productId = p.productId
    LEFT JOIN StoreVariantPrice svp
        ON svp.productVariant.variantId = v.variantId
        AND svp.store.storeId = :storeId
        AND svp.isActive = true
    WHERE p.isActive = true
      AND v.isActive = true
""")
    List<PosProductProjection> findAllForPos(@Param("storeId") Integer storeId);
}
