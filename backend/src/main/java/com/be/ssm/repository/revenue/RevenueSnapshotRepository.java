package com.be.ssm.repository.revenue;

import com.be.ssm.entities.revenue.RevenueSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevenueSnapshotRepository extends JpaRepository<RevenueSnapshot, Integer> {
}
