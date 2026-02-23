package com.be.ssm.mapper.sales;

import com.be.ssm.dto.common.PageDTO;
import com.be.ssm.dto.request.sale.OrderCreateRequest;
import com.be.ssm.dto.request.sale.OrderUpdateRequest;
import com.be.ssm.dto.response.sale.OrderResponse;
import com.be.ssm.entities.sales.Orders;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;

@Mapper(
        componentModel = "spring",
        uses = {OrderItemMapper.class}
)
public interface OrdersMapper {

    OrderResponse toOrderResponse(Orders orders);

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
