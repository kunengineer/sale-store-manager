package com.be.ssm.service.sale;

import com.be.ssm.dto.request.sale.CustomerCreateRequest;
import com.be.ssm.dto.request.sale.CustomerUpdateRequest;
import com.be.ssm.dto.response.sale.CustomerResponse;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {
    CustomerResponse getById(Integer id);

    CustomerResponse create(CustomerCreateRequest request);

    CustomerResponse update(CustomerUpdateRequest request, Integer customerId);
}
