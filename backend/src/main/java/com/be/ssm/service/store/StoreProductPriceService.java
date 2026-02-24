package com.be.ssm.service.store;

import com.be.ssm.dto.request.store.StoreProductPriceCreateRequest;
import com.be.ssm.dto.request.store.StoreProductPriceUpdateRequest;
import com.be.ssm.dto.response.store.StoreProductPriceResponse;
import org.springframework.stereotype.Service;

@Service
public interface StoreProductPriceService {
    StoreProductPriceResponse create(StoreProductPriceCreateRequest request);
    StoreProductPriceResponse update(Integer id, StoreProductPriceUpdateRequest request);
}
