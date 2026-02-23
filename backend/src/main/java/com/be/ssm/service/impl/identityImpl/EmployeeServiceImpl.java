package com.be.ssm.service.impl.identityImpl;

import com.be.ssm.dto.request.identity.EmployeeCreateRequest;
import com.be.ssm.dto.request.identity.EmployeeUpdateRequest;
import com.be.ssm.dto.response.identity.EmployeeResponse;
import com.be.ssm.entities.identity.Employees;
import com.be.ssm.entities.identity.Roles;
import com.be.ssm.entities.identity.WorkShifts;
import com.be.ssm.entities.store.Stores;
import com.be.ssm.mapper.identity.EmployeeMapper;
import com.be.ssm.repository.identity.EmployeesRepository;
import com.be.ssm.repository.identity.RolesRepository;
import com.be.ssm.repository.identity.WorkShiftsRepository;
import com.be.ssm.repository.store.StoresRepository;
import com.be.ssm.service.identity.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeesRepository repository;
    private final RolesRepository rolesRepository;
    private final StoresRepository storesRepository;
    private final WorkShiftsRepository workShiftsRepository;
    private final EmployeeMapper mapper;

    @Override
    public EmployeeResponse getById(Integer id) {
        log.info("Getting employee by id {}", id);

        return mapper.toEmployeeResponse(findById(id));
    }

    @Override
    public EmployeeResponse create(EmployeeCreateRequest request) {
        log.info("Create new employee");
        Roles role = findRoleById(request.getRoleId());
        Stores store = storesRepository.findById(request.getStoreId())
                .orElseThrow();
        WorkShifts workShifts = findWorkShiftById(request.getWorkShiftId());

        Employees employee = mapper.toEmployeeEntity(request);
        employee.setRole(role);
        employee.setStore(store);
        employee.setWorkShift(workShifts);

        return mapper.toEmployeeResponse(repository.save(employee));
    }

    @Override
    public EmployeeResponse update(EmployeeUpdateRequest request, Integer id) {
        log.info("Update employee with id {}", id);
        Roles role = findRoleById(request.getRoleId());
        WorkShifts workShifts = findWorkShiftById(request.getWorkShiftId());

        Employees employee = findById(id);
        employee.setRole(role);
        employee.setWorkShift(workShifts);

        mapper.updateEntityFromRequest(request, employee);

        return mapper.toEmployeeResponse(repository.save(employee));
    }

    private Employees findById(Integer id) {
        log.info("Finding employee by id {}", id);

        return repository.findById(id)
                .orElseThrow();
    }

    private Roles findRoleById(Integer id) {
        log.info("Finding role by id {}", id);

        return rolesRepository.findById(id)
                .orElseThrow();
    }

    private WorkShifts findWorkShiftById(Integer id) {
        log.info("Finding work shift by id {}", id);

        return workShiftsRepository.findById(id)
                .orElseThrow();
    }
}