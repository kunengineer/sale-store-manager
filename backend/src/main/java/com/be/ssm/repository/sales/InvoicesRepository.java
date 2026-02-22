package com.be.ssm.repository.sales;

import com.be.ssm.entities.sales.Invoices;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoicesRepository extends JpaRepository<Invoices, Integer> {
}
