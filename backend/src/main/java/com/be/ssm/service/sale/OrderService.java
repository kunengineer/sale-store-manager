package com.be.ssm.service.sale;

import com.be.ssm.dto.common.PageDTO;
import com.be.ssm.dto.filter.OrderFilter;
import com.be.ssm.dto.request.sale.OrderCreateRequest;
import com.be.ssm.dto.request.sale.OrderUpdateRequest;
import com.be.ssm.dto.response.sale.OrderResponse;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    OrderResponse getById(Integer id);

    OrderResponse create(OrderCreateRequest request);

    OrderResponse update(OrderUpdateRequest request, Integer orderId);

    PageDTO<OrderResponse> getAll(int page, int size, OrderFilter filter);
}
