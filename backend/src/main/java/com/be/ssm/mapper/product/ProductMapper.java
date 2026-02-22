package com.be.ssm.mapper.product;

import com.be.ssm.dto.request.product.ProductCreateRequest;
import com.be.ssm.dto.request.product.ProductUpdateRequest;
import com.be.ssm.dto.response.product.ProductResponse;
import com.be.ssm.entities.product.Products;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toProductResponse(Products product);

    Products toProductEntity(ProductCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy
            = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(
            ProductUpdateRequest request,
            @MappingTarget Products product
    );
}
