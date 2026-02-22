package com.be.ssm.mapper.identity;

import com.be.ssm.dto.request.identity.EmployeeCreateRequest;
import com.be.ssm.dto.request.identity.EmployeeUpdateRequest;
import com.be.ssm.dto.response.identity.EmployeeResponse;
import com.be.ssm.entities.identity.Employees;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    @Mapping(source = "role.roleName", target = "roleName")
    EmployeeResponse toEmployeeResponse(Employees employees);

    Employees toEmployeeEntity(EmployeeCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(EmployeeUpdateRequest request,
                                 @MappingTarget Employees entity);

}
