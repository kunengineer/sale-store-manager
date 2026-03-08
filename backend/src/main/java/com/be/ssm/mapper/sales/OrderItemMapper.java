package com.be.ssm.mapper.sales;

import com.be.ssm.dto.request.sale.OrderItemCreateRequest;
import com.be.ssm.dto.request.sale.OrderItemUpdateRequest;
import com.be.ssm.dto.response.sale.OrderItemResponse;
import com.be.ssm.entities.sales.OrderItems;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "variantId", expression = "java(getVariantId(orderItems))")
    @Mapping(target = "variantName", source = "productVariants.variantName")
    @Mapping(target = "productName", source = "productVariants.product.productName")
    OrderItemResponse toOrderItemResponse(OrderItems orderItems);

    default Integer getVariantId(OrderItems orderItems) {
        if (orderItems == null || orderItems.getProductVariants() == null) {
            return null;
        }
        return orderItems.getProductVariants().getVariantId();
    }

    OrderItems toOrderItemEntity(OrderItemCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(OrderItemUpdateRequest request,
                                 @MappingTarget OrderItems entity);

}
