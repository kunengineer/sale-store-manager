package com.be.ssm.mapper.product;

import com.be.ssm.dto.request.product.CategoryCreateRequest;
import com.be.ssm.dto.request.product.CategoryUpdateRequest;
import com.be.ssm.dto.response.product.CategoriesResponse;
import com.be.ssm.entities.product.Categories;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoriesMapper {
    CategoriesResponse toCategoriesResponse(Categories category);

    Categories toCategoriesEntity(CategoryCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy
            = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(
            CategoryUpdateRequest request,
            @MappingTarget Categories category
    );
}