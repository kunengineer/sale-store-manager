package com.be.ssm.service.impl.saleImpl;

import com.be.ssm.dto.common.PageDTO;
import com.be.ssm.dto.filter.OrderFilter;
import com.be.ssm.dto.request.sale.OrderCreateRequest;
import com.be.ssm.dto.request.sale.OrderUpdateRequest;
import com.be.ssm.dto.response.sale.OrderResponse;
import com.be.ssm.entities.sales.Customers;
import com.be.ssm.entities.sales.Orders;
import com.be.ssm.entities.store.StoreTables;
import com.be.ssm.mapper.sales.OrdersMapper;
import com.be.ssm.repository.sales.CustomersRepository;
import com.be.ssm.repository.sales.OrdersRepository;
import com.be.ssm.repository.store.StoreTablesRepository;
import com.be.ssm.service.sale.OrderService;
import com.be.ssm.specification.OrderSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrdersRepository repository;
    private final StoreTablesRepository storeTablesRepository;
    private final CustomersRepository customersRepository;

    private final OrdersMapper mapper;

    @Override
    public OrderResponse getById(Integer id) {
        log.info("Getting order by id {}", id);

        return mapper.toOrderResponse(findById(id));
    }

    @Override
    public OrderResponse create(OrderCreateRequest request) {
        log.info("Create new order");

        StoreTables table = findStoreTableById(request.getTableId());
        Customers customer = request.getCustomerId() != null ? findCustomerById(request.getCustomerId()) : null;

        Orders order = mapper.toOrderEntity(request);
        order.setStoreTables(table);
        order.setCustomers(customer);

        return mapper.toOrderResponse(repository.save(order));
    }

    @Override
    public OrderResponse update(OrderUpdateRequest request, Integer id) {
        log.info("Update order");

        Orders order = findById(id);
        StoreTables table = findStoreTableById(request.getTableId());
        Customers customer = request.getCustomerId() != null ? findCustomerById(request.getCustomerId()) : null;

        mapper.updateEntityFromRequest(request, order);
        order.setStoreTables(table);
        order.setCustomers(customer);

        return mapper.toOrderResponse(repository.save(order));
    }

    @Override
    public PageDTO<OrderResponse> getAll(int page, int size, OrderFilter filter) {
        Specification<Orders> specification = OrderSpecification.filter(filter);
        Pageable pageable = PageRequest.of(page-1, size);
        return mapper.toPageDTO(repository.findAll(specification, pageable));
    }

    private Orders findById(Integer id) {
        log.info("Finding order by id {}", id);

        return repository.findById(id)
                .orElseThrow();
    }

    private StoreTables findStoreTableById(Integer id) {
        log.info("Finding store table by id {}", id);

        return storeTablesRepository.findById(id)
                .orElseThrow();
    }

    private Customers findCustomerById(Integer id) {
        log.info("Finding customers by id {}", id);

        return customersRepository.findById(id)
                .orElseThrow();
    }
}
