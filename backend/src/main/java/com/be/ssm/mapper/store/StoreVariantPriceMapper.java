package com.be.ssm.mapper.store;

import com.be.ssm.dto.request.store.StoreVariantPriceCreateRequest;
import com.be.ssm.dto.request.store.StoreVariantPriceUpdateRequest;
import com.be.ssm.dto.response.store.StoreVariantPriceResponse;
import com.be.ssm.entities.store.StoreVariantPrice;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StoreVariantPriceMapper {
    StoreVariantPriceResponse toStoreVariantPriceResponse(StoreVariantPrice storeVariantPrice);

    StoreVariantPrice toStoreVariantPriceEntity(StoreVariantPriceCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy
            = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(StoreVariantPriceUpdateRequest request,
                                 @MappingTarget StoreVariantPrice entity);
}
