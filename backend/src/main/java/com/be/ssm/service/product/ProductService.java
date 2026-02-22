package com.be.ssm.service.product;

import com.be.ssm.dto.request.product.ProductCreateRequest;
import com.be.ssm.dto.request.product.ProductUpdateRequest;
import com.be.ssm.dto.response.product.ProductResponse;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    ProductResponse getById(Integer id);

    ProductResponse create(ProductCreateRequest request);

    ProductResponse update(ProductUpdateRequest request, Integer productId);
}
