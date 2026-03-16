package com.be.ssm.service.impl.identityImpl;


import com.be.ssm.dto.request.identity.RoleCreateRequest;
import com.be.ssm.dto.request.identity.RoleUpdateRequest;
import com.be.ssm.dto.response.identity.RoleResponse;
import com.be.ssm.entities.identity.Roles;
import com.be.ssm.entities.store.Stores;
import com.be.ssm.exceptions.CustomException;
import com.be.ssm.exceptions.Error;
import com.be.ssm.mapper.identity.RoleMapper;
import com.be.ssm.repository.identity.RolesRepository;
import com.be.ssm.repository.store.StoresRepository;
import com.be.ssm.service.identity.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RolesRepository repository;
    private final RoleMapper mapper;

    private final StoresRepository storesRepository;

    @Override
    public RoleResponse getById(Integer id) {
        log.info("Getting role by id {}", id);

        return mapper.toRoleResponse(findById(id));
    }

    @Override
    public RoleResponse create(RoleCreateRequest request) {
        log.info("Create new role");

        Roles role = mapper.toRoleEntity(request);

        return mapper.toRoleResponse(repository.save(role));
    }

    @Override
    public RoleResponse update(RoleUpdateRequest request, Integer id) {
        log.info("Update role with id {}", id);

        Roles role = findById(id);

        mapper.updateEntityFromRequest(request, role);

        return mapper.toRoleResponse(repository.save(role));
    }

    @Override
    public Roles initRoleForOwn(Integer storeId, Stores store) {

        Roles roles = Roles.builder()
                .roleName("STORE OWNER")
                .description("Manager for store")
                .isSystem(false)
                .stores(store)
                .build();

        return repository.save(roles);
    }

    private Roles findById(Integer id) {
        log.info("Finding role by id {}", id);

        return repository.findById(id)
                .orElseThrow(()-> new CustomException(Error.ROLE_NOT_FOUND));
    }

    private Stores findStoreById(Integer storeId) {
        log.info("Finding store by id {}", storeId);
        return storesRepository.findById(storeId)
                .orElseThrow(()-> new CustomException(Error.STORE_NOT_FOUND));
    }
}