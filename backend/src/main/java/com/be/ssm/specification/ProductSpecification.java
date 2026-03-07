package com.be.ssm.specification;

import com.be.ssm.dto.filter.ProductFilter;
import com.be.ssm.entities.product.Products;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Products> filter(ProductFilter filter) {
        return (root, query, cb) -> {
            if (filter == null) {
                return cb.conjunction();
            }
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getStoreId() == null) {
                predicates.add(cb.equal(
                        root.get("store").get("storeId"),
                        filter.getCategoryId()
                ));
            }

            // Filter theo categoryId
            if (filter.getCategoryId() != null) {
                predicates.add(cb.equal(
                        root.get("category").get("categoryId"),
                        filter.getCategoryId()
                ));
            }

            // Filter theo trạng thái active
            if (filter.getIsActive() != null) {
                predicates.add(cb.equal(
                        root.get("isActive"),
                        filter.getIsActive()
                ));
            }

            // Filter keyword (productName hoặc productCode)
            if (filter.getKeyword() != null && !filter.getKeyword().isBlank()) {
                String keyword = "%" + filter.getKeyword().toLowerCase() + "%";
                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("productName")), keyword),
                                cb.like(cb.lower(root.get("productCode")), keyword)
                        )
                );
            }

            // Filter basePrice min/max
            if (filter.getMinBasePrice() != null && filter.getMaxBasePrice() != null) {
                predicates.add(cb.between(
                        root.get("basePrice"),
                        filter.getMinBasePrice(),
                        filter.getMaxBasePrice()
                ));
            } else if (filter.getMinBasePrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("basePrice"),
                        filter.getMinBasePrice()
                ));
            } else if (filter.getMaxBasePrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("basePrice"),
                        filter.getMaxBasePrice()
                ));
            }

            // Filter costPrice min/max
            if (filter.getMinCostPrice() != null && filter.getMaxCostPrice() != null) {
                predicates.add(cb.between(
                        root.get("costPrice"),
                        filter.getMinCostPrice(),
                        filter.getMaxCostPrice()
                ));
            } else if (filter.getMinCostPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("costPrice"),
                        filter.getMinCostPrice()
                ));
            } else if (filter.getMaxCostPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("costPrice"),
                        filter.getMaxCostPrice()
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
    public static Specification<Products> byStore(Integer storeId) {
        return (root, query, cb) -> {
            if (storeId == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("store").get("storeId"),
                    storeId
            );
        };
    }
}
