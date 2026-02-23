package com.be.ssm.specification;

import com.be.ssm.dto.filter.StoreTableFilter;
import com.be.ssm.entities.store.StoreTables;
import com.be.ssm.entities.store.StoreZones;
import com.be.ssm.entities.store.Stores;
import com.be.ssm.enums.store.TableStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class StoreTableSpecification {

    public static Specification<StoreTables> filter(StoreTableFilter filter) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) {
                return cb.conjunction();
            }

            // =========================
            // zoneId
            // =========================
            if (filter.getZoneId() != null) {
                Join<StoreTables, StoreZones> zoneJoin = root.join("zone", JoinType.INNER);
                predicates.add(cb.equal(zoneJoin.get("zoneId"), filter.getZoneId()));
            }

            // =========================
            // storeId (join zone -> store)
            // =========================
            if (filter.getStoreId() != null) {
                Join<StoreTables, StoreZones> zoneJoin = root.join("zone", JoinType.INNER);
                Join<StoreZones, Stores> storeJoin = zoneJoin.join("store", JoinType.INNER);
                predicates.add(cb.equal(storeJoin.get("storeId"), filter.getStoreId()));
            }

            // =========================
            // tableCode LIKE (case insensitive)
            // =========================
            if (StringUtils.hasText(filter.getTableCode())) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("tableCode")),
                                "%" + filter.getTableCode().toLowerCase() + "%"
                        )
                );
            }

            // =========================
            // isActive → map theo status nếu có quy ước
            // =========================
            if (filter.getIsActive() != null) {
                    predicates.add(cb.notEqual(root.get("status"), filter.getIsActive()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}