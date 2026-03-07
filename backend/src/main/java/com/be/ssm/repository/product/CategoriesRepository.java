package com.be.ssm.repository.product;

import com.be.ssm.entities.product.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoriesRepository extends JpaRepository<Categories, Integer>, JpaSpecificationExecutor<Categories> {
    boolean existsByCategoryNameAndStoreStoreId(String name, Integer storeId);
}
