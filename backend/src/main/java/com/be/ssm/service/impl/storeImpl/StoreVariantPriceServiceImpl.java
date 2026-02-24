package com.be.ssm.service.impl.storeImpl;

import com.be.ssm.dto.request.store.StoreVariantPriceCreateRequest;
import com.be.ssm.dto.request.store.StoreVariantPriceUpdateRequest;
import com.be.ssm.dto.response.store.StoreVariantPriceResponse;
import com.be.ssm.entities.product.ProductVariants;
import com.be.ssm.entities.store.StoreVariantPrice;
import com.be.ssm.entities.store.Stores;
import com.be.ssm.mapper.store.StoreVariantPriceMapper;
import com.be.ssm.repository.product.ProductVariantsRepository;
import com.be.ssm.repository.store.StoreVariantPriceRepository;
import com.be.ssm.repository.store.StoresRepository;
import com.be.ssm.service.store.StoreVariantPriceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StoreVariantPriceServiceImpl implements StoreVariantPriceService {
    private final StoreVariantPriceMapper storeVariantPriceMapper;
    private final StoreVariantPriceRepository storeVariantPriceRepository;
    private final StoresRepository storeRepository;
    private final ProductVariantsRepository variantRepository;

    @Override
    public StoreVariantPriceResponse create(StoreVariantPriceCreateRequest request) {
        Stores store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + request.getStoreId()));

        ProductVariants variant = variantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new RuntimeException("Variant not found with id: " + request.getVariantId()));

        StoreVariantPrice storeVariantPrice = storeVariantPriceMapper.toStoreVariantPriceEntity(request);
        storeVariantPrice.setStore(store);
        storeVariantPrice.setProductVariant(variant);

        return storeVariantPriceMapper.toStoreVariantPriceResponse(
                storeVariantPriceRepository.save(storeVariantPrice)
        );
    }

    @Override
    public StoreVariantPriceResponse update(Integer id, StoreVariantPriceUpdateRequest request) {
        StoreVariantPrice storeVariantPrice = getStoreVariantPriceById(id);
        storeVariantPriceMapper.updateEntityFromRequest(request, storeVariantPrice);
        return storeVariantPriceMapper.toStoreVariantPriceResponse(
                storeVariantPriceRepository.save(storeVariantPrice)
        );
    }

    private StoreVariantPrice getStoreVariantPriceById(Integer id) {
        return storeVariantPriceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store variant price not found with id: " + id));
    }
}
