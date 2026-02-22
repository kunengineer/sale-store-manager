package com.be.ssm.service.impl.productImpl;

import com.be.ssm.dto.request.product.ProductVariantCreateRequest;
import com.be.ssm.dto.request.product.ProductVariantUpdateRequest;
import com.be.ssm.dto.response.product.ProductVariantResponse;
import com.be.ssm.entities.product.ProductVariants;
import com.be.ssm.mapper.product.ProductVariantMapper;
import com.be.ssm.repository.product.ProductVariantsRepository;
import com.be.ssm.service.product.ProductVariantService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {
    private final ProductVariantsRepository repository;
    private final ProductVariantMapper mapper;

    @Override
    public ProductVariantResponse getById(Integer id) {
        return mapper.toProductVariantResponse(findById(id));
    }

    @Override
    public ProductVariantResponse create(ProductVariantCreateRequest request) {
        ProductVariants variant = mapper.toProductVariantEntity(request);
        return mapper.toProductVariantResponse(repository.save(variant));
    }

    @Override
    public ProductVariantResponse update(ProductVariantUpdateRequest request, Integer variantId) {
        ProductVariants variant = findById(variantId);
        mapper.updateEntityFromRequest(request, variant);
        return mapper.toProductVariantResponse(repository.save(variant));
    }

    private ProductVariants findById(Integer id) {
        return repository.findById(id)
                .orElseThrow();
    }
}
