package com.be.ssm.service.impl.saleImpl;

import com.be.ssm.dto.common.PageDTO;
import com.be.ssm.dto.filter.OrderFilter;
import com.be.ssm.dto.request.sale.OrderCreateRequest;
import com.be.ssm.dto.request.sale.OrderItemCreateRequest;
import com.be.ssm.dto.request.sale.OrderUpdateRequest;
import com.be.ssm.dto.response.sale.OrderResponse;
import com.be.ssm.entities.identity.Employees;
import com.be.ssm.entities.product.ProductVariants;
import com.be.ssm.entities.sales.Customers;
import com.be.ssm.entities.sales.OrderItems;
import com.be.ssm.entities.sales.Orders;
import com.be.ssm.entities.store.StoreTables;
import com.be.ssm.enums.store.TableStatus;
import com.be.ssm.exceptions.CustomException;
import com.be.ssm.exceptions.Error;
import com.be.ssm.mapper.sales.OrdersMapper;
import com.be.ssm.repository.product.ProductVariantsRepository;
import com.be.ssm.repository.sales.CustomersRepository;
import com.be.ssm.repository.sales.OrderItemsRepository;
import com.be.ssm.repository.sales.OrdersRepository;
import com.be.ssm.repository.store.StoreTablesRepository;
import com.be.ssm.service.identity.EmployeeService;
import com.be.ssm.service.impl.accountImpl.OurUserDetailsService;
import com.be.ssm.service.sale.OrderItemService;
import com.be.ssm.service.sale.OrderService;
import com.be.ssm.specification.OrderSpecification;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrdersRepository repository;
    private final StoreTablesRepository storeTablesRepository;
    private final CustomersRepository customersRepository;
    private final ProductVariantsRepository productVariantsRepository;
    private final OrderItemsRepository orderItemsRepository;


    private final OrdersMapper mapper;
    private final OrderItemService orderItemService;
    private final OurUserDetailsService userDetailsService;
    private final EmployeeService employeeService;


    @Override
    public OrderResponse getById(Integer id) {
        log.info("Getting order by id {}", id);

        return mapper.toOrderResponse(findById(id));
    }

    @Override
    public OrderResponse getByTable(Integer tableId) {

        return mapper.toOrderResponse(repository.findOrderProgressByTable(tableId));
    }

    @Transactional
    @Override
    public OrderResponse create(OrderCreateRequest request) {
        log.info("Create new order");

        StoreTables table = findStoreTableById(request.getTableId());
        if (!table.getStatus().equals(TableStatus.AVAILABLE)) {
            throw new CustomException(Error.STORE_TABLE_UNAVAILABLE);
        }
        Employees employee = employeeService.getEmployeeForAccount(userDetailsService.getAccountAuth().getAccountId());
        Customers customer = request.getCustomerId() != null ? findCustomerById(request.getCustomerId()) : null;

        Orders order = mapper.toOrderEntity(request);
        order.setStoreTables(table);
        order.setEmployees(employee);
        order.setCustomers(customer);

        Integer storeId = employee.getStore().getStoreId();

        List<OrderItems> items = orderItemService.buildItems(request.getItems(), order, storeId);

        log.info("Build item");

        order.setOrderItems(items);

        order.recalculate();

        log.info("recalculate");

        repository.save(order);

        log.info("Saved order");

        table.setStatus(TableStatus.OCCUPIED);
        storeTablesRepository.save(table);

        return mapper.toOrderResponse(order);
    }

    @Override
    public OrderResponse addItems(Integer orderId, List<OrderItemCreateRequest> newItems) {
        Orders order = findById(orderId);

        Integer storeId = order.getStoreTables().getZone().getStore().getStoreId();

        List<OrderItems> itemsToAdd = orderItemService.buildItems(newItems, order, storeId);

        List<OrderItems> savedItems = orderItemsRepository.saveAll(itemsToAdd);

        savedItems.forEach(order::addItem);

        order.recalculate();

        return mapper.toOrderResponse(repository.save(order));
    }

    @Override
    public void moveOrder(Integer originTable, StoreTables storeTable) {
        Orders orders = repository.findOrderProgressByTable(originTable);
        orders.setStoreTables(storeTable);
        repository.save(orders);
    }

    @Override
    public void mergeOrder(Integer originTable, Integer targetTable) {
        Orders origin = repository.findOrderProgressByTable(originTable);
        Orders target = repository.findOrderProgressByTable(targetTable);

        // Build map variantId -> item của target để lookup O(1)
        Map<Integer, OrderItems> targetItemMap = target.getOrderItems().stream()
                .collect(Collectors.toMap(
                        item -> item.getProductVariants().getVariantId(),
                        item -> item
                ));

        for (OrderItems originItem : origin.getOrderItems()) {
            Integer variantId = originItem.getProductVariants().getVariantId();

            if (targetItemMap.containsKey(variantId)) {
                // Cùng variant → cộng quantity, @PreUpdate tự recalculate lineTotal
                OrderItems targetItem = targetItemMap.get(variantId);
                targetItem.setQuantity(targetItem.getQuantity() + originItem.getQuantity());

            } else {
                // Chưa có → copy item sang target
                OrderItems copied = OrderItems.builder()
                        .productVariants(originItem.getProductVariants())
                        .quantity(originItem.getQuantity())
                        .unitPrice(originItem.getUnitPrice())
                        .discountPct(originItem.getDiscountPct())
                        .discountAmt(originItem.getDiscountAmt())
                        .note(originItem.getNote())
                        .build();
                target.addItem(copied);
            }
        }

        // orphanRemoval = true → clear() sẽ tự delete các item của origin khỏi DB
        origin.getOrderItems().clear();

        repository.save(target);   // cascade ALL → save luôn items mới/updated
        repository.delete(origin);
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
        order.recalculate();

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



}
