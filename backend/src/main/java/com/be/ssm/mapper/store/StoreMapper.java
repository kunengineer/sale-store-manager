package com.be.ssm.mapper.store;

import com.be.ssm.dto.request.store.StoreCreateRequest;
import com.be.ssm.dto.request.store.StoreUpdateRequest;
import com.be.ssm.dto.response.store.StoreResponse;
import com.be.ssm.entities.store.Stores;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StoreMapper {
    StoreResponse toStoreResponse(Stores store);

    Stores toStoreEntity(StoreCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy
            = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(
            StoreUpdateRequest request,
            @MappingTarget Stores product
    );


}
