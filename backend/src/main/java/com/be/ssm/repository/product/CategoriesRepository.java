package com.be.ssm.repository.product;

import com.be.ssm.entities.product.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<Categories, Integer> {
}
