package com.be.ssm.specification;

import com.be.ssm.dto.filter.CategoryFilter;
import com.be.ssm.entities.product.Categories;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.ArrayList;

public class CategorySpecification {
    public static Specification<Categories> filter(CategoryFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) {
                return cb.conjunction();
            }

            // parentId
            if (filter.getParentId() != null) {
                Join<Object, Object> parentJoin = root.join("parent", JoinType.LEFT);
                predicates.add(cb.equal(parentJoin.get("id"), filter.getParentId()));
            }

            // categoryName LIKE
            if (StringUtils.hasText(filter.getCategoryName())) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("categoryName")),
                                "%" + filter.getCategoryName().toLowerCase() + "%"
                        )
                );
            }

            // isActive
            if (filter.getIsActive() != null) {
                predicates.add(cb.equal(root.get("isActive"), filter.getIsActive()));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}
