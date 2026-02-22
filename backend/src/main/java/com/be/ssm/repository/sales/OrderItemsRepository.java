package com.be.ssm.repository.sales;

import com.be.ssm.entities.sales.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Integer> {
}
