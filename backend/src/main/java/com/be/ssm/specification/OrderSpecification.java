package com.be.ssm.specification;

import com.be.ssm.dto.filter.OrderFilter;
import com.be.ssm.entities.sales.Orders;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderSpecification {
    public static Specification<Orders> filter(OrderFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) {
                return cb.conjunction();
            }

            // orderNumber LIKE
            if (StringUtils.hasText(filter.getOrderNumber())) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("orderNumber")),
                                "%" + filter.getOrderNumber().toLowerCase() + "%"
                        )
                );
            }

            // storeTableId
            if (filter.getStoreTableId() != null) {
                Join<Object, Object> tableJoin = root.join("storeTable", JoinType.LEFT);
                predicates.add(cb.equal(tableJoin.get("id"), filter.getStoreTableId()));
            }

            // customerId
            if (filter.getCustomerId() != null) {
                Join<Object, Object> customerJoin = root.join("customer", JoinType.LEFT);
                predicates.add(cb.equal(customerJoin.get("id"), filter.getCustomerId()));
            }

            // status
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }

            // storeId
            if (filter.getStoreId() != null) {
                Join<Object, Object> storeJoin = root.join("store", JoinType.LEFT);
                predicates.add(cb.equal(storeJoin.get("id"), filter.getStoreId()));
            }

            // fromDate (>= createdAt)
            if (filter.getFromDate() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("createdAt"),
                                filter.getFromDate()
                        )
                );
            }

            // toDate (<= createdAt)
            if (filter.getToDate() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("createdAt"),
                                filter.getToDate()
                        )
                );
            }

            // minTotal (>= totalAmount)
            if (filter.getMinTotal() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("totalAmount"),
                                filter.getMinTotal()
                        )
                );
            }

            // maxTotal (<= totalAmount)
            if (filter.getMaxTotal() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("totalAmount"),
                                filter.getMaxTotal()
                        )
                );
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}
