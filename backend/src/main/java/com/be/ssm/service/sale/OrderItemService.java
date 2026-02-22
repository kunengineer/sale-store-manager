package com.be.ssm.service.sale;

import com.be.ssm.dto.request.sale.OrderItemCreateRequest;
import com.be.ssm.dto.request.sale.OrderItemUpdateRequest;
import com.be.ssm.dto.response.sale.OrderItemResponse;
import org.springframework.stereotype.Service;

@Service
public interface OrderItemService {
    OrderItemResponse getById(Integer id);

    OrderItemResponse create(OrderItemCreateRequest request);

    OrderItemResponse update(OrderItemUpdateRequest request, Integer orderItemId);
}
