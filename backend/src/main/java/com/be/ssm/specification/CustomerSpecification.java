package com.be.ssm.specification;

import com.be.ssm.dto.request.sale.CustomerFilerRequest;
import com.be.ssm.entities.sales.Customers;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification {

    public static Specification<Customers> filter(CustomerFilerRequest filter) {

        return (root, query, cb) -> {

            if (filter == null) {
                return cb.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();

            // =========================
            // CUSTOMER CODE (LIKE - case insensitive)
            // =========================
            if (StringUtils.hasText(filter.getCustomerCode())) {

                String keyword = filter.getCustomerCode().trim().toLowerCase();

                predicates.add(
                        cb.like(
                                cb.lower(root.get("customerCode")),
                                "%" + keyword + "%"
                        )
                );
            }

            // =========================
            // FULL NAME (LIKE - case insensitive)
            // =========================
            if (StringUtils.hasText(filter.getFullName())) {

                String keyword = filter.getFullName().trim().toLowerCase();

                predicates.add(
                        cb.like(
                                cb.lower(root.get("fullName")),
                                "%" + keyword + "%"
                        )
                );
            }

            // =========================
            // STORE ID (> 0)
            // =========================
            if (filter.getStoreId() != null && filter.getStoreId() > 0) {

                Join<Object, Object> storeJoin =
                        root.join("store", JoinType.INNER);

                predicates.add(
                        cb.equal(
                                storeJoin.get("storeId"),
                                filter.getStoreId()
                        )
                );
            }

            return predicates.isEmpty()
                    ? cb.conjunction()
                    : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}