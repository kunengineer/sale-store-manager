package com.be.ssm.service.impl.saleImpl;

import com.be.ssm.dto.request.sale.OrderItemCreateRequest;
import com.be.ssm.dto.request.sale.OrderItemUpdateRequest;
import com.be.ssm.dto.response.sale.OrderItemResponse;
import com.be.ssm.entities.product.ProductVariants;
import com.be.ssm.entities.sales.OrderItems;
import com.be.ssm.entities.sales.Orders;
import com.be.ssm.mapper.sales.OrderItemMapper;
import com.be.ssm.repository.product.ProductVariantsRepository;
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
    private final ProductVariantsRepository productVariantsRepository;
    private final OrderItemMapper mapper;

    @Override
    public OrderItemResponse getById(Integer id) {
        log.info("Getting order item by id {}", id);

        return mapper.toOrderItemResponse(findById(id));
    }

    @Override
    public OrderItemResponse create(OrderItemCreateRequest request) {
        log.info("Create new order item");
        ProductVariants productVariants = findProductVariantById(request.getProductVariantId());
        Orders order = findOrderById(request.getOrderId());

        OrderItems orderItem = mapper.toOrderItemEntity(request);
        orderItem.setProductVariants(productVariants);
        orderItem.setOrder(order);

        return mapper.toOrderItemResponse(repository.save(orderItem));
    }

    @Override
    public OrderItemResponse update(OrderItemUpdateRequest request, Integer id) {
        log.info("Update order item with id {}", id);

        OrderItems orderItem = findById(id);
        ProductVariants productVariants = findProductVariantById(request.getProductVariantId());
        Orders order = findOrderById(request.getOrderId());

        mapper.updateEntityFromRequest(request, orderItem);
        orderItem.setOrder(order);
        orderItem.setProductVariants(productVariants);

        return mapper.toOrderItemResponse(repository.save(orderItem));
    }

    private OrderItems findById(Integer id) {
        log.info("Finding order item by id {}", id);

        return repository.findById(id)
                .orElseThrow();
    }

    private ProductVariants findProductVariantById(Integer id) {
        log.info("Finding product variant by id {}", id);

        return productVariantsRepository.findById(id)
                .orElseThrow();
    }

    private Orders findOrderById(Integer id) {
        log.info("Finding orders by id {}", id);

        return ordersRepository.findById(id)
                .orElseThrow();
    }
}
