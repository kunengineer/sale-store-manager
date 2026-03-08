package com.be.ssm.repository.sales;

import com.be.ssm.entities.sales.Orders;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrdersRepository extends JpaRepository<Orders, Integer>, JpaSpecificationExecutor<Orders> {
    @Query(nativeQuery = true, value = """
        SELECT o.*
        FROM orders o
        INNER JOIN store_tables t 
            ON o.store_table_id = t.table_id
        WHERE t.table_id = :tableId
        AND o.status = 'IN_PROGRESS'
""")
    Orders findOrderByTable(@Param("tableId") Integer tableId);
}
