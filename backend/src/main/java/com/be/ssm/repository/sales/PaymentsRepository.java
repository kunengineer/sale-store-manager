package com.be.ssm.repository.sales;

import com.be.ssm.entities.sales.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentsRepository extends JpaRepository<Payments, Integer> {
}
