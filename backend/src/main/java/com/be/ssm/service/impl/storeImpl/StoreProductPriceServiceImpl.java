package com.be.ssm.service.impl.storeImpl;

import com.be.ssm.dto.request.store.StoreProductPriceCreateRequest;
import com.be.ssm.dto.request.store.StoreProductPriceUpdateRequest;
import com.be.ssm.dto.response.store.StoreProductPriceResponse;
import com.be.ssm.entities.product.Products;
import com.be.ssm.entities.store.StoreProductPrice;
import com.be.ssm.entities.store.StoreVariantPrice;
import com.be.ssm.entities.store.Stores;
import com.be.ssm.exceptions.CustomException;
import com.be.ssm.exceptions.Error;
import com.be.ssm.mapper.store.StoreProductPriceMapper;
import com.be.ssm.repository.product.ProductsRepository;
import com.be.ssm.repository.store.StoreProductPriceRepository;
import com.be.ssm.repository.store.StoresRepository;
import com.be.ssm.service.store.StoreProductPriceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StoreProductPriceServiceImpl implements StoreProductPriceService {
    private final StoreProductPriceRepository repository;
    private final StoreProductPriceMapper mapper;
    private final StoresRepository storeRepository;
    private final ProductsRepository productRepository;

    @Override
    public StoreProductPriceResponse create(StoreProductPriceCreateRequest request) {

        Stores stores = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new CustomException(Error.STORE_NOT_FOUND));

        Products products = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new CustomException(Error.PRODUCT_NOT_FOUND));

        StoreProductPrice entity = mapper.toStoreProductPriceEntity(request);
        entity.setStore(stores);
        entity.setProduct(products);

        return mapper.toStoreProductPriceResponse(repository.save(entity));
    }

    @Override
    public StoreProductPriceResponse update(Integer id, StoreProductPriceUpdateRequest request) {
        StoreProductPrice storeProductPrice = findEntityById(id);
        mapper.updateEntityFromRequest(request, storeProductPrice);
        return mapper.toStoreProductPriceResponse(repository.save(storeProductPrice));
    }


    private StoreProductPrice findEntityById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomException(Error.STORE_PRODUCT_PRICE_NOT_FOUND));
    }
}
