package com.be.ssm.mapper.identity;

import com.be.ssm.dto.request.identity.WorkShiftCreateRequest;
import com.be.ssm.dto.request.identity.WorkShiftUpdateRequest;
import com.be.ssm.dto.response.identity.WorkShiftResponse;
import com.be.ssm.entities.identity.WorkShifts;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface WorkShiftMapper {
    WorkShiftResponse toWorkShiftResponse(WorkShifts entity);

    WorkShifts toWorkShiftEntity(WorkShiftCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(WorkShiftUpdateRequest request,
                                 @MappingTarget WorkShifts entity);
}
