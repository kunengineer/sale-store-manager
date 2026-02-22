package com.be.ssm.mapper.store;

import com.be.ssm.dto.request.store.StoreTableCreateRequest;
import com.be.ssm.dto.request.store.StoreTableUpdateRequest;
import com.be.ssm.dto.response.store.StoreTableResponse;
import com.be.ssm.entities.store.StoreTables;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface StoreTableMapper {
    StoreTableResponse toStoreTableResponse(StoreTables storeTables);

    StoreTables toStoreTableEntity(StoreTableCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy
            = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(StoreTableUpdateRequest request,
                                 @MappingTarget StoreTables entity);
}
