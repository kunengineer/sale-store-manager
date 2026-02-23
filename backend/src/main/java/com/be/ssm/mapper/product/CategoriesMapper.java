package com.be.ssm.mapper.product;

import com.be.ssm.dto.common.PageDTO;
import com.be.ssm.dto.request.product.CategoryCreateRequest;
import com.be.ssm.dto.request.product.CategoryUpdateRequest;
import com.be.ssm.dto.response.product.CategoriesResponse;
import com.be.ssm.dto.response.product.ProductResponse;
import com.be.ssm.entities.product.Categories;
import com.be.ssm.entities.product.Products;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

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

    default PageDTO<CategoriesResponse> toPageDTO(Page<Categories> page) {
        return PageDTO.<CategoriesResponse>builder()
                .content(page.getContent()
                        .stream()
                        .map(this::toCategoriesResponse)
                        .toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}