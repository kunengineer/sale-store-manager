package com.be.ssm.service.identity;

import com.be.ssm.dto.request.identity.RoleCreateRequest;
import com.be.ssm.dto.request.identity.RoleUpdateRequest;
import com.be.ssm.dto.response.identity.RoleResponse;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {
    RoleResponse getById(Integer id);

    RoleResponse create(RoleCreateRequest request);

    RoleResponse update(RoleUpdateRequest request, Integer roleId);
}
