package com.be.ssm.service.impl.productImpl;

import com.be.ssm.dto.common.PageDTO;
import com.be.ssm.dto.filter.CategoryFilter;
import com.be.ssm.dto.request.product.CategoryCreateRequest;
import com.be.ssm.dto.request.product.CategoryUpdateRequest;
import com.be.ssm.dto.response.product.CategoriesResponse;
import com.be.ssm.entities.product.Categories;
import com.be.ssm.exceptions.CustomException;
import com.be.ssm.exceptions.Error;
import com.be.ssm.mapper.product.CategoriesMapper;
import com.be.ssm.repository.product.CategoriesRepository;
import com.be.ssm.service.product.CategoriesService;
import com.be.ssm.specification.CategorySpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
        if (repository.existsByCategoryNameAndStoreStoreId(request.getCategoryName(), request.getStoreId())) {
            throw new CustomException(Error.CATEGORY_NAME_ALREADY_EXISTS);
        }
        Categories category = mapper.toCategoriesEntity(request);
        return mapper.toCategoriesResponse(repository.save(category));
    }

    @Override
    public CategoriesResponse update(CategoryUpdateRequest request, Integer categoryId) {
        Categories category = findById(categoryId);
        mapper.updateEntityFromRequest(request, category);
        return mapper.toCategoriesResponse(repository.save(category));
    }

    @Override
    public PageDTO<CategoriesResponse> getAllCategories(int page, int size, CategoryFilter filter) {
        Specification<Categories> specification = CategorySpecification.filter(filter);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("sortOrder").ascending());
        return mapper.toPageDTO(repository.findAll(specification, pageable));
    }

    private Categories findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(()-> new CustomException(Error.CATEGORIES_NOT_FOUND));
    }
}
