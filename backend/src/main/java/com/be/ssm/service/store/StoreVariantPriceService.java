package com.be.ssm.service.store;

import com.be.ssm.dto.request.store.StoreVariantPriceCreateRequest;
import com.be.ssm.dto.request.store.StoreVariantPriceUpdateRequest;
import com.be.ssm.dto.response.store.StoreVariantPriceResponse;
import org.springframework.stereotype.Service;

@Service
public interface StoreVariantPriceService {
    StoreVariantPriceResponse create(StoreVariantPriceCreateRequest request);

    StoreVariantPriceResponse update(Integer id, StoreVariantPriceUpdateRequest request);

}
