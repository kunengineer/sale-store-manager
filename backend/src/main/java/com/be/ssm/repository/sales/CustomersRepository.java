package com.be.ssm.repository.sales;

import com.be.ssm.entities.sales.Customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomersRepository extends JpaRepository<Customers, Integer>, JpaSpecificationExecutor<Customers> {
}
