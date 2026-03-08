package com.be.ssm.service.sale;

import com.be.ssm.dto.common.PageDTO;
import com.be.ssm.dto.request.sale.CustomerCreateRequest;
import com.be.ssm.dto.request.sale.CustomerFilerRequest;
import com.be.ssm.dto.request.sale.CustomerUpdateRequest;
import com.be.ssm.dto.response.sale.CustomerResponse;
import com.be.ssm.dto.response.sale.PosCustomerResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {
    CustomerResponse getById(Integer id);

    CustomerResponse create(CustomerCreateRequest request);

    CustomerResponse update(CustomerUpdateRequest request, Integer customerId);

    PageDTO<CustomerResponse> filter(CustomerFilerRequest request, int page, int size);

    List<PosCustomerResponse> filterPos(CustomerFilerRequest request);
}
