package com.be.ssm.service.product;

import com.be.ssm.dto.request.product.CategoryCreateRequest;
import com.be.ssm.dto.request.product.CategoryUpdateRequest;
import com.be.ssm.dto.response.product.CategoriesResponse;
import org.springframework.stereotype.Service;

@Service
public interface CategoriesService {
    CategoriesResponse getById(Integer id);

    CategoriesResponse create(CategoryCreateRequest request);

    CategoriesResponse update(CategoryUpdateRequest request, Integer categoryId);
}
