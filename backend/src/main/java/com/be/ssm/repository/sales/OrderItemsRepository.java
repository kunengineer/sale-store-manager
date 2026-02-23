package com.be.ssm.repository.sales;

import com.be.ssm.entities.sales.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Integer> {
    List<OrderItems> findOrderItemsByOrder_OrderId(Integer orderOrderId);
}
