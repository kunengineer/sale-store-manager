package com.be.ssm.service.impl.productImpl;

import com.be.ssm.dto.common.PageDTO;
import com.be.ssm.dto.filter.ProductFilter;
import com.be.ssm.dto.request.product.ProductCreateRequest;
import com.be.ssm.dto.request.product.ProductUpdateRequest;
import com.be.ssm.dto.response.product.ProductResponse;
import com.be.ssm.entities.product.Categories;
import com.be.ssm.entities.product.Products;
import com.be.ssm.exceptions.CustomException;
import com.be.ssm.exceptions.Error;
import com.be.ssm.mapper.product.ProductMapper;
import com.be.ssm.repository.product.CategoriesRepository;
import com.be.ssm.repository.product.ProductsRepository;
import com.be.ssm.service.product.CategoriesService;
import com.be.ssm.service.product.ProductService;
import com.be.ssm.specification.ProductSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductsRepository repository;
    private final ProductMapper mapper;
    private final CategoriesRepository categoriesRepository;


    @Override
    public ProductResponse getById(Integer id) {
        return mapper.toProductResponse(findById(id));
    }

    @Override
    public ProductResponse create(ProductCreateRequest request) {

        Categories category = categoriesRepository.findById(request.getCategoryId())
                .orElseThrow();

        Products product = mapper.toProductEntity(request);
        product.setCategory(category);

        return mapper.toProductResponse(repository.save(product));
    }

    @Override
    public ProductResponse update(ProductUpdateRequest request, Integer productId) {
        Products product = findById(productId);
        mapper.updateEntityFromRequest(request, product);
        return mapper.toProductResponse(repository.save(product));
    }

    @Override
    public PageDTO<ProductResponse> getAll(int page, int size, ProductFilter filter) {
        Specification<Products> specification = ProductSpecification.filter(filter);
        Pageable pageable = PageRequest.of(page - 1, size);
        return mapper.toPageDTO(repository.findAll(specification, pageable));
    }

    private Products findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(()-> new CustomException(Error.PRODUCT_NOT_FOUND));
    }
}
