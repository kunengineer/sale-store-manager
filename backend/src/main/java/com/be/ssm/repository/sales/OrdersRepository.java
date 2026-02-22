package com.be.ssm.repository.sales;

import com.be.ssm.entities.sales.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
}
