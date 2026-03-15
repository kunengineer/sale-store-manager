package com.be.ssm.mapper.store;

import com.be.ssm.dto.request.store.RegisterNewStore;
import com.be.ssm.dto.request.store.StoreCreateRequest;
import com.be.ssm.dto.request.store.StoreUpdateRequest;
import com.be.ssm.dto.response.store.StoreResponse;
import com.be.ssm.entities.store.Stores;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StoreMapper {
    StoreResponse toStoreResponse(Stores store);

    Stores toStoreEntity(RegisterNewStore request);

    Stores toStoreEntity(StoreCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy
            = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(
            StoreUpdateRequest request,
            @MappingTarget Stores product
    );

    default List<StoreResponse>  toStoreResponseList(List<Stores> stores){
        return stores.stream()
                .map(this::toStoreResponse)
                .toList();
    }


}
