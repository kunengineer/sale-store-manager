package com.be.ssm.mapper.store;

import com.be.ssm.dto.common.PageDTO;
import com.be.ssm.dto.request.store.StoreZonesCreateRequest;
import com.be.ssm.dto.request.store.StoreZonesUpdateRequest;
import com.be.ssm.dto.response.store.StoreZoneResponse;
import com.be.ssm.entities.store.StoreZones;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface StoreZoneMapper {
    StoreZoneResponse toStoreZoneResponse(StoreZones storeZones);

    StoreZones toStoreZoneEntity(StoreZonesCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy
            = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(StoreZonesUpdateRequest request,
                                 @MappingTarget StoreZones entity);

    default PageDTO<StoreZoneResponse> toPageDTO(Page<StoreZones> page) {
        return PageDTO.<StoreZoneResponse>builder()
                .content(page.getContent()
                        .stream()
                        .map(this::toStoreZoneResponse)
                        .toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

}
