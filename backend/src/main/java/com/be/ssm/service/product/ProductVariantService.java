package com.be.ssm.service.product;

import com.be.ssm.dto.request.product.ProductVariantCreateRequest;
import com.be.ssm.dto.request.product.ProductVariantUpdateRequest;
import com.be.ssm.dto.response.product.ProductVariantResponse;
import org.springframework.stereotype.Service;

@Service
public interface ProductVariantService {
    ProductVariantResponse getById(Integer id);

    ProductVariantResponse create(ProductVariantCreateRequest request);

    ProductVariantResponse update(ProductVariantUpdateRequest request, Integer variantId);
}
