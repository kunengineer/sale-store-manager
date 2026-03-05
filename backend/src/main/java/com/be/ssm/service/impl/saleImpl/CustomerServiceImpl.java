package com.be.ssm.service.impl.saleImpl;

import com.be.ssm.dto.common.PageDTO;
import com.be.ssm.dto.request.sale.CustomerCreateRequest;
import com.be.ssm.dto.request.sale.CustomerFilerRequest;
import com.be.ssm.dto.request.sale.CustomerUpdateRequest;
import com.be.ssm.dto.response.sale.CustomerResponse;
import com.be.ssm.entities.sales.Customers;
import com.be.ssm.entities.store.Stores;
import com.be.ssm.exceptions.CustomException;
import com.be.ssm.exceptions.Error;
import com.be.ssm.mapper.sales.CustomerMapper;
import com.be.ssm.repository.sales.CustomersRepository;
import com.be.ssm.repository.store.StoresRepository;
import com.be.ssm.service.sale.CustomerService;
import com.be.ssm.specification.CustomerSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomersRepository repository;
    private final StoresRepository storesRepository;

    private final CustomerMapper mapper;

    @Override
    public CustomerResponse getById(Integer id) {
        log.info("Getting customer by id {}", id);

        return mapper.toCustomerResponse(findById(id));
    }

    @Override
    public CustomerResponse create(CustomerCreateRequest request) {
        log.info("Create new customer");

        Stores store = storesRepository.findById(request.getStoreId())
                .orElseThrow(() -> new CustomException(Error.STORE_NOT_FOUND));

        Customers customer = mapper.toCustomerEntity(request);
        customer.setStore(store);

        return mapper.toCustomerResponse(repository.save(customer));
    }

    @Override
    public CustomerResponse update(CustomerUpdateRequest request, Integer id) {
        log.info("Update customer with id {}", id);

        Customers customer = findById(id);

        mapper.updateEntityFromRequest(request, customer);

        return mapper.toCustomerResponse(repository.save(customer));
    }

    @Override
    public PageDTO<CustomerResponse> filteṛ(CustomerFilerRequest request, int page, int size) {
        // Page bắt đầu từ 1 ở API → convert về 0-based
        int pageIndex = Math.max(page - 1, 0);

        Pageable pageable = PageRequest.of(pageIndex, size);

        Page<Customers> pageData = repository.findAll(
                CustomerSpecification.filter(request),
                pageable
        );

        List<CustomerResponse> content = pageData.getContent()
                .stream()
                .map(mapper::toCustomerResponse)
                .toList();

        return PageDTO.<CustomerResponse>builder()
                .page(page)
                .size(size)
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .content(content)
                .build();
    }

    private Customers findById(Integer id) {
        log.info("Finding customer by id {}", id);

        return repository.findById(id)
                .orElseThrow(()-> new CustomException(Error.CUSTOMER_NOT_FOUND));
    }

}
