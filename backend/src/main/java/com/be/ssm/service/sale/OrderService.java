package com.be.ssm.service.sale;

import com.be.ssm.dto.common.PageDTO;
import com.be.ssm.dto.filter.OrderFilter;
import com.be.ssm.dto.request.sale.OrderCreateRequest;
import com.be.ssm.dto.request.sale.OrderItemCreateRequest;
import com.be.ssm.dto.request.sale.OrderUpdateRequest;
import com.be.ssm.dto.response.sale.OrderResponse;
import com.be.ssm.entities.store.StoreTables;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    OrderResponse getById(Integer id);

    OrderResponse getByTable(Integer tableId);

    OrderResponse create(OrderCreateRequest request);

    OrderResponse addItems(Integer orderId, List<OrderItemCreateRequest> newItems);

    void moveOrder(Integer originTable, StoreTables storeTable);
    void mergeOrder(Integer originTable, Integer targetTable);

    OrderResponse update(OrderUpdateRequest request, Integer orderId);

    PageDTO<OrderResponse> getAll(int page, int size, OrderFilter filter);
}
