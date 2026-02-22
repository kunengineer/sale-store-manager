package com.be.ssm.mapper.store;

import com.be.ssm.dto.request.store.StoreZonesCreateRequest;
import com.be.ssm.dto.request.store.StoreZonesUpdateRequest;
import com.be.ssm.dto.response.store.StoreZoneResponse;
import com.be.ssm.entities.store.StoreZones;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface StoreZoneMapper {
    StoreZoneResponse toStoreZoneResponse(StoreZones storeZones);

    StoreZones toStoreZoneEntity(StoreZonesCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy
            = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(StoreZonesUpdateRequest request,
                                 @MappingTarget StoreZones entity);

}
