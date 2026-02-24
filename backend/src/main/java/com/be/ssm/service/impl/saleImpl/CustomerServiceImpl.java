package com.be.ssm.service.impl.saleImpl;

import com.be.ssm.dto.request.sale.CustomerCreateRequest;
import com.be.ssm.dto.request.sale.CustomerUpdateRequest;
import com.be.ssm.dto.response.sale.CustomerResponse;
import com.be.ssm.entities.sales.Customers;
import com.be.ssm.exceptions.CustomException;
import com.be.ssm.exceptions.Error;
import com.be.ssm.mapper.sales.CustomerMapper;
import com.be.ssm.repository.sales.CustomersRepository;
import com.be.ssm.service.sale.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomersRepository repository;
    private final CustomerMapper mapper;

    @Override
    public CustomerResponse getById(Integer id) {
        log.info("Getting customer by id {}", id);

        return mapper.toCustomerResponse(findById(id));
    }

    @Override
    public CustomerResponse create(CustomerCreateRequest request) {
        log.info("Create new customer");

        Customers customer = mapper.toCustomerEntity(request);

        return mapper.toCustomerResponse(repository.save(customer));
    }

    @Override
    public CustomerResponse update(CustomerUpdateRequest request, Integer id) {
        log.info("Update customer with id {}", id);

        Customers customer = findById(id);

        mapper.updateEntityFromRequest(request, customer);

        return mapper.toCustomerResponse(repository.save(customer));
    }

    private Customers findById(Integer id) {
        log.info("Finding customer by id {}", id);

        return repository.findById(id)
                .orElseThrow(()-> new CustomException(Error.CUSTOMER_NOT_FOUND));
    }

}
