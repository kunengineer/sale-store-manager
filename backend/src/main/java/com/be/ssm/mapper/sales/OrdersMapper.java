package com.be.ssm.mapper.sales;

import com.be.ssm.dto.request.sale.OrderCreateRequest;
import com.be.ssm.dto.request.sale.OrderUpdateRequest;
import com.be.ssm.dto.response.sale.OrderResponse;
import com.be.ssm.entities.sales.Orders;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface OrdersMapper {
    OrderResponse toOrderResponse(Orders orders);

    Orders toOrderEntity(OrderCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(OrderUpdateRequest request,
                                 @MappingTarget Orders entity);
}
