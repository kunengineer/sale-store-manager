package com.be.ssm.mapper.store;

import com.be.ssm.dto.request.store.StoreProductPriceCreateRequest;
import com.be.ssm.dto.request.store.StoreProductPriceUpdateRequest;
import com.be.ssm.dto.response.store.StoreProductPriceResponse;
import com.be.ssm.entities.store.StoreProductPrice;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StoreProductPriceMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "store", ignore = true)
    @Mapping(target = "product", ignore = true)
    StoreProductPrice toStoreProductPriceEntity(StoreProductPriceCreateRequest request);

    @Mapping(target = "storeId", source = "store.storeId")
    @Mapping(target = "storeName", source = "store.storeName")
    @Mapping(target = "productId", source = "product.productId")
    @Mapping(target = "productName", source = "product.productName")
    StoreProductPriceResponse toStoreProductPriceResponse(StoreProductPrice entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(StoreProductPriceUpdateRequest request,
                      @MappingTarget StoreProductPrice entity);
}
