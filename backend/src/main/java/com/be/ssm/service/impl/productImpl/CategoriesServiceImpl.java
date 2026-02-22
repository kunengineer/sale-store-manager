package com.be.ssm.service.impl.productImpl;

import com.be.ssm.dto.request.product.CategoryCreateRequest;
import com.be.ssm.dto.request.product.CategoryUpdateRequest;
import com.be.ssm.dto.response.product.CategoriesResponse;
import com.be.ssm.entities.product.Categories;
import com.be.ssm.mapper.product.CategoriesMapper;
import com.be.ssm.repository.product.CategoriesRepository;
import com.be.ssm.service.product.CategoriesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {
    private final CategoriesRepository repository;
    private final CategoriesMapper mapper;
    @Override
    public CategoriesResponse getById(Integer id) {
        return mapper.toCategoriesResponse(findById(id));
    }

    @Override
    public CategoriesResponse create(CategoryCreateRequest request) {
        Categories category = mapper.toCategoriesEntity(request);
        return mapper.toCategoriesResponse(repository.save(category));
    }

    @Override
    public CategoriesResponse update(CategoryUpdateRequest request, Integer categoryId) {
        Categories category = findById(categoryId);
        mapper.updateEntityFromRequest(request, category);
        return mapper.toCategoriesResponse(repository.save(category));
    }

    private Categories findById(Integer id) {
        return repository.findById(id)
                .orElseThrow();
    }
}
