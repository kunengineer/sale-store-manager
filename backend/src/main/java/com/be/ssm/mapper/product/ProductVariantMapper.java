package com.be.ssm.mapper.product;

import com.be.ssm.dto.request.product.ProductVariantCreateRequest;
import com.be.ssm.dto.request.product.ProductVariantUpdateRequest;
import com.be.ssm.dto.response.product.ProductVariantResponse;
import com.be.ssm.entities.product.ProductVariants;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {
    ProductVariants toProductVariantEntity(ProductVariantCreateRequest request);

    ProductVariantResponse toProductVariantResponse(ProductVariants productVariant);

    @BeanMapping(nullValuePropertyMappingStrategy
            = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(
            ProductVariantUpdateRequest request,
            @MappingTarget ProductVariants productVariant
    );


}
