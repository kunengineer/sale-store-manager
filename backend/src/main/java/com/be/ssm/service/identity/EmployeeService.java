package com.be.ssm.service.identity;

import com.be.ssm.dto.request.identity.EmployeeCreateRequest;
import com.be.ssm.dto.request.identity.EmployeeUpdateRequest;
import com.be.ssm.dto.response.identity.EmployeeResponse;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeService {
    EmployeeResponse getById(Integer id);

    EmployeeResponse create(EmployeeCreateRequest request);

    EmployeeResponse update(EmployeeUpdateRequest request, Integer employeeId);
}
