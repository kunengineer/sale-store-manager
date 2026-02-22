package com.be.ssm.repository.product;

import com.be.ssm.entities.product.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Products, Integer> {
}
