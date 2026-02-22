package com.be.ssm.service.impl.saleImpl;

import com.be.ssm.dto.request.sale.OrderItemCreateRequest;
import com.be.ssm.dto.request.sale.OrderItemUpdateRequest;
import com.be.ssm.dto.response.sale.OrderItemResponse;
import com.be.ssm.entities.sales.OrderItems;
import com.be.ssm.entities.sales.Orders;
import com.be.ssm.mapper.sales.OrderItemMapper;
import com.be.ssm.repository.product.ProductsRepository;
import com.be.ssm.repository.sales.OrderItemsRepository;
import com.be.ssm.repository.sales.OrdersRepository;
import com.be.ssm.service.sale.OrderItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemsRepository repository;
    private final OrdersRepository ordersRepository;
    // private final ProductsRepository productsRepository;
    private final OrderItemMapper mapper;

    @Override
    public OrderItemResponse getById(Integer id) {
        log.info("Getting order item by id {}", id);

        return mapper.toResponse(findById(id));
    }

    @Override
    public OrderItemResponse create(OrderItemCreateRequest request) {
        log.info("Create new order item");
        // IS1: ProductVariant
        Orders order = findOrderById(request.getOrderId());

        OrderItems orderItem = mapper.fromCreateToEntity(request);
        orderItem.setOrder(order);

        return mapper.toResponse(repository.save(orderItem));
    }

    @Override
    public OrderItemResponse update(OrderItemUpdateRequest request, Integer id) {
        log.info("Update order item with id {}", id);

        OrderItems orderItem = findById(id);
        // IS1: ProductVariant
        Orders order = findOrderById(request.getOrderId());

        mapper.updateEntityFromRequest(request, orderItem);
        orderItem.setOrder(order);

        return mapper.toResponse(repository.save(orderItem));
    }

    private OrderItems findById(Integer id) {
        log.info("Finding order item by id {}", id);

        return repository.findById(id)
                .orElseThrow();
    }

    private Orders findOrderById(Integer id) {
        log.info("Finding orders by id {}", id);

        return ordersRepository.findById(id)
                .orElseThrow();
    }
}
