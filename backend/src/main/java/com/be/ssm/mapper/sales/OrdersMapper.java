package com.be.ssm.mapper.sales;

import com.be.ssm.dto.common.PageDTO;
import com.be.ssm.dto.request.sale.OrderCreateRequest;
import com.be.ssm.dto.request.sale.OrderUpdateRequest;
import com.be.ssm.dto.response.sale.OrderResponse;
import com.be.ssm.entities.sales.Orders;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(
        componentModel = "spring",
        uses = {OrderItemMapper.class}
)
public interface OrdersMapper {

    OrderResponse toOrderResponse(Orders orders);

    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "orderNumber", ignore = true)
    @Mapping(target = "customers", ignore = true)
    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "storeTables", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "grandTotal", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "orderItems", source = "items")
    Orders toOrderEntity(OrderCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(OrderUpdateRequest request,
                                 @MappingTarget Orders entity);

    default PageDTO<OrderResponse> toPageDTO(Page<Orders> page) {
        return PageDTO.<OrderResponse>builder()
                .content(page.getContent()
                        .stream()
                        .map(this::toOrderResponse)
                        .toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
