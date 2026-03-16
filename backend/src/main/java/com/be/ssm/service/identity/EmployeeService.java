package com.be.ssm.service.identity;

import com.be.ssm.dto.request.identity.EmployeeCreateRequest;
import com.be.ssm.dto.request.identity.EmployeeUpdateRequest;
import com.be.ssm.dto.response.identity.EmployeeResponse;
import com.be.ssm.entities.identity.Employees;
import com.be.ssm.entities.store.Stores;
import org.apache.catalina.Store;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeService {
    EmployeeResponse getById(Integer id);

    EmployeeResponse getCurrentEmployee();

    EmployeeResponse create(EmployeeCreateRequest request);

    Employees createOwnerForStore(EmployeeCreateRequest request, Stores store);

    EmployeeResponse update(EmployeeUpdateRequest request, Integer employeeId);

    Employees getEmployeeForAccount(Integer accountId);
}
