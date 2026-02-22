package com.be.ssm.repository.product;

import com.be.ssm.entities.product.ProductVariants;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantsRepository extends JpaRepository<ProductVariants, Integer> {
}
