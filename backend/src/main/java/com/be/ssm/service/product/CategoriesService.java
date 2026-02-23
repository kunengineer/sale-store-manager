package com.be.ssm.service.product;

import com.be.ssm.dto.common.PageDTO;
import com.be.ssm.dto.filter.CategoryFilter;
import com.be.ssm.dto.request.product.CategoryCreateRequest;
import com.be.ssm.dto.request.product.CategoryUpdateRequest;
import com.be.ssm.dto.response.product.CategoriesResponse;
import org.springframework.stereotype.Service;

@Service
public interface CategoriesService {
    CategoriesResponse getById(Integer id);

    CategoriesResponse create(CategoryCreateRequest request);

    CategoriesResponse update(CategoryUpdateRequest request, Integer categoryId);

    PageDTO<CategoriesResponse> getAllCategories(int page, int size, CategoryFilter filter);
}
