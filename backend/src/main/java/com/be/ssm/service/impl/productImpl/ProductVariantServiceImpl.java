package com.be.ssm.service.impl.productImpl;

import com.be.ssm.dto.request.product.ProductVariantCreateRequest;
import com.be.ssm.dto.request.product.ProductVariantUpdateRequest;
import com.be.ssm.dto.response.product.ProductVariantResponse;
import com.be.ssm.entities.product.ProductVariants;
import com.be.ssm.entities.product.Products;
import com.be.ssm.exceptions.CustomException;
import com.be.ssm.exceptions.Error;
import com.be.ssm.mapper.product.ProductVariantMapper;
import com.be.ssm.repository.product.ProductVariantsRepository;
import com.be.ssm.repository.product.ProductsRepository;
import com.be.ssm.service.product.ProductVariantService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {
    private final ProductVariantsRepository repository;
    private final ProductVariantMapper mapper;
    private final ProductsRepository productsRepository;

    @Override
    public ProductVariantResponse getById(Integer id) {
        return mapper.toProductVariantResponse(findById(id));
    }

    @Override
    public ProductVariantResponse create(ProductVariantCreateRequest request) {
//        if (repository.existsBySku(request.getSku())) {
//            // Handle duplicate SKU case, e.g., throw an exception or return an error response
//        }

        Products product = productsRepository.findById(request.getProductId())
                .orElseThrow();

        ProductVariants variant = mapper.toProductVariantEntity(request);
        variant.setProduct(product);

        return mapper.toProductVariantResponse(repository.save(variant));
    }

    @Override
    public ProductVariantResponse update(ProductVariantUpdateRequest request, Integer variantId) {
        ProductVariants variant = findById(variantId);
        mapper.updateEntityFromRequest(request, variant);
        return mapper.toProductVariantResponse(repository.save(variant));
    }

    @Override
    public List<ProductVariantResponse> findAll(Integer productId) {

        return repository.findAllByProduct_ProductId(productId)
                .stream()
                .map(mapper::toProductVariantResponse)
                .toList();
    }

    private ProductVariants findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(()-> new CustomException(Error.PRODUCT_VARIANT_NOT_FOUND));
    }
}
