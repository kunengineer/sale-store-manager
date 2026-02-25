package com.be.ssm.service.sale;

import com.be.ssm.dto.request.sale.OrderItemCreateRequest;
import com.be.ssm.dto.request.sale.OrderItemUpdateRequest;
import com.be.ssm.dto.response.sale.OrderItemResponse;
import com.be.ssm.entities.sales.OrderItems;
import com.be.ssm.entities.sales.Orders;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderItemService {
    OrderItemResponse getById(Integer id);

    List<OrderItems> buildItems(List<OrderItemCreateRequest> itemRequests, Orders order, Integer storeId);

    OrderItemResponse update(OrderItemUpdateRequest request, Integer orderItemId);
}
