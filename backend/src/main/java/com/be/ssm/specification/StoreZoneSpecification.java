package com.be.ssm.specification;

import com.be.ssm.dto.filter.StoreZoneFilter;
import com.be.ssm.entities.store.StoreZones;
import com.be.ssm.entities.store.Stores;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class StoreZoneSpecification {

    public static Specification<StoreZones> filter(StoreZoneFilter filter) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) {
                return cb.conjunction();
            }

            if (filter.getStoreId() != null) {
                Join<StoreZones, Stores> storeJoin = root.join("store", JoinType.INNER);
                predicates.add(cb.equal(storeJoin.get("storeId"), filter.getStoreId()));
            }

            if (StringUtils.hasText(filter.getZoneName())) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("zoneName")),
                                "%" + filter.getZoneName().toLowerCase() + "%"
                        )
                );
            }

            if (filter.getZoneType() != null) {
                predicates.add(cb.equal(root.get("zoneType"), filter.getZoneType()));
            }

            if (filter.getCapacity() != null) {
                predicates.add(cb.equal(root.get("capacity"), filter.getCapacity()));
            }

            if (filter.getIsActive() != null) {
                predicates.add(cb.equal(root.get("isActive"), filter.getIsActive()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}