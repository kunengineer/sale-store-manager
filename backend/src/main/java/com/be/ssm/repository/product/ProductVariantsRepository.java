package com.be.ssm.repository.product;

import com.be.ssm.entities.product.ProductVariants;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductVariantsRepository extends JpaRepository<ProductVariants, Integer> {
    boolean existsBySku(String sku);

    List<ProductVariants> findAllByProduct_ProductId(Integer productId);
}
