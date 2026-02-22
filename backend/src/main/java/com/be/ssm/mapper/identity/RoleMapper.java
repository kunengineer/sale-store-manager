package com.be.ssm.mapper.identity;

import com.be.ssm.dto.request.identity.RoleCreateRequest;
import com.be.ssm.dto.request.identity.RoleUpdateRequest;
import com.be.ssm.dto.response.identity.RoleResponse;
import com.be.ssm.entities.identity.Roles;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponse toRoleResponse(Roles role);

    Roles toRoleEntity(RoleCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(RoleUpdateRequest request,
                                 @MappingTarget Roles entity);
}
