package com.be.ssm.service.impl.saleImpl;

import com.be.ssm.dto.common.PageDTO;
import com.be.ssm.dto.filter.OrderFilter;
import com.be.ssm.dto.request.sale.OrderCreateRequest;
import com.be.ssm.dto.request.sale.OrderItemCreateRequest;
import com.be.ssm.dto.request.sale.OrderUpdateRequest;
import com.be.ssm.dto.response.sale.OrderItemResponse;
import com.be.ssm.dto.response.sale.OrderResponse;
import com.be.ssm.entities.identity.Employees;
import com.be.ssm.entities.product.ProductVariants;
import com.be.ssm.entities.sales.Customers;
import com.be.ssm.entities.sales.OrderItems;
import com.be.ssm.entities.sales.Orders;
import com.be.ssm.entities.store.StoreProductPrice;
import com.be.ssm.entities.store.StoreTables;
import com.be.ssm.entities.store.StoreVariantPrice;
import com.be.ssm.exceptions.CustomException;
import com.be.ssm.exceptions.Error;
import com.be.ssm.helper.OrderTotalCalculator;
import com.be.ssm.mapper.sales.OrderItemMapper;
import com.be.ssm.mapper.sales.OrdersMapper;
import com.be.ssm.repository.identity.EmployeesRepository;
import com.be.ssm.repository.product.ProductVariantsRepository;
import com.be.ssm.repository.sales.CustomersRepository;
import com.be.ssm.repository.sales.OrdersRepository;
import com.be.ssm.repository.store.StoreProductPriceRepository;
import com.be.ssm.repository.store.StoreTablesRepository;
import com.be.ssm.repository.store.StoreVariantPriceRepository;
import com.be.ssm.service.sale.OrderItemService;
import com.be.ssm.service.sale.OrderService;
import com.be.ssm.specification.OrderSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrdersRepository repository;
    private final StoreTablesRepository storeTablesRepository;
    private final CustomersRepository customersRepository;
    private final EmployeesRepository employeesRepository;
    private final ProductVariantsRepository productVariantsRepository;

    private final OrderTotalCalculator orderTotalCalculator;

    private final OrdersMapper mapper;
    private final OrderItemService orderItemService;


    @Override
    public OrderResponse getById(Integer id) {
        log.info("Getting order by id {}", id);

        return mapper.toOrderResponse(findById(id));
    }

    @Override
    public OrderResponse create(OrderCreateRequest request) {
        log.info("Create new order");

        StoreTables table = findStoreTableById(request.getTableId());
        // Employees employee = findEmployeeById(request.get());
        Customers customer = request.getCustomerId() != null ? findCustomerById(request.getCustomerId()) : null;

        Orders order = mapper.toOrderEntity(request);
        order.setStoreTables(table);
        // order.setEmployees();
        order.setCustomers(customer);

        Integer storeId = order.getStoreTables().getZone().getStore().getStoreId();

        List<OrderItems> items = orderItemService.buildItems(request.getItems(), order, storeId);

        order.setOrderItems(items);

        orderTotalCalculator.recalculateFromItems(order);

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
        orderTotalCalculator.recalculateFromItems(order);

        return mapper.toOrderResponse(repository.save(order));
    }

    @Override
    public PageDTO<OrderResponse> getAll(int page, int size, OrderFilter filter) {
        Specification<Orders> specification = OrderSpecification.filter(filter);
        Pageable pageable = PageRequest.of(page - 1, size);
        return mapper.toPageDTO(repository.findAll(specification, pageable));
    }

    private Orders findById(Integer id) {
        log.info("Finding order by id {}", id);

        return repository.findById(id)
                .orElseThrow(()-> new CustomException(Error.ORDER_ITEM_NOT_FOUND));
    }

    private ProductVariants findProductVariantById(Integer id) {
        log.info("Finding product variant by id {}", id);

        return productVariantsRepository.findById(id)
                .orElseThrow(()-> new CustomException(Error.PRODUCT_VARIANT_NOT_FOUND));
    }

    private StoreTables findStoreTableById(Integer id) {
        log.info("Finding store table by id {}", id);

        return storeTablesRepository.findById(id)
                .orElseThrow(()-> new CustomException(Error.STORE_TABLE_NOT_FOUND));
    }

    private Customers findCustomerById(Integer id) {
        log.info("Finding customers by id {}", id);

        return customersRepository.findById(id)
                .orElseThrow(()-> new CustomException(Error.CUSTOMER_NOT_FOUND));
    }

    private  Employees findEmployeeById(Integer id) {
        log.info("Finding employee by id {}", id);

        return employeesRepository.findById(id)
                .orElseThrow();
    }


}
