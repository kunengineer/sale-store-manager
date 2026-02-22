package com.be.ssm.repository.identity;

import com.be.ssm.entities.identity.Employees;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeesRepository extends JpaRepository<Employees, Integer> {
}
