package com.be.ssm.mapper.product;

import com.be.ssm.dto.common.PageDTO;
import com.be.ssm.dto.request.product.ProductCreateRequest;
import com.be.ssm.dto.request.product.ProductUpdateRequest;
import com.be.ssm.dto.response.product.ProductResponse;
import com.be.ssm.entities.product.Products;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;

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

    default PageDTO<ProductResponse> toPageDTO(Page<Products> page) {
        return PageDTO.<ProductResponse>builder()
                .content(page.getContent()
                        .stream()
                        .map(this::toProductResponse)
                        .toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
