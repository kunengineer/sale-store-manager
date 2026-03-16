package com.be.ssm.service.impl.identityImpl;

import com.be.ssm.dto.request.account.AccountCreateRequest;
import com.be.ssm.dto.request.identity.EmployeeCreateRequest;
import com.be.ssm.dto.request.identity.EmployeeUpdateRequest;
import com.be.ssm.dto.response.identity.EmployeeResponse;
import com.be.ssm.entities.account.Accounts;
import com.be.ssm.entities.identity.Employees;
import com.be.ssm.entities.identity.Roles;
import com.be.ssm.entities.identity.WorkShifts;
import com.be.ssm.entities.store.Stores;
import com.be.ssm.exceptions.CustomException;
import com.be.ssm.exceptions.Error;
import com.be.ssm.mapper.account.AccountMapper;
import com.be.ssm.mapper.identity.EmployeeMapper;
import com.be.ssm.mapper.identity.RoleMapper;
import com.be.ssm.repository.identity.EmployeesRepository;
import com.be.ssm.repository.identity.RolesRepository;
import com.be.ssm.repository.identity.WorkShiftsRepository;
import com.be.ssm.repository.store.StoresRepository;
import com.be.ssm.service.account.AccountService;
import com.be.ssm.service.identity.EmployeeService;
import com.be.ssm.service.identity.RoleService;
import com.be.ssm.service.impl.accountImpl.OurUserDetailsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Store;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeesRepository repository;
    private final StoresRepository storesRepository;
    private final WorkShiftsRepository workShiftsRepository;

    private final OurUserDetailsService userDetailsService;
    private final RoleService roleService;
    private final AccountService accountService;

    private final EmployeeMapper mapper;
    private final AccountMapper accountMapper;
    private final RoleMapper roleMapper;

    @Override
    public EmployeeResponse getById(Integer id) {
        log.info("Getting employee by id {}", id);

        return mapper.toEmployeeResponse(findById(id));
    }

    @Override
    public EmployeeResponse getCurrentEmployee() {
        Accounts account = userDetailsService.getAccountAuth();

        return mapper.toEmployeeResponse(repository.findByAccount(account)
                .orElseThrow(()-> new CustomException(Error.ACCOUNT_NOT_FOUND)));
    }

    @Override
    public EmployeeResponse create(EmployeeCreateRequest request) {
        log.info("Create new employee");

        Roles role = findRoleById(request.getRoleId());

        return mapper.toEmployeeResponse(saveAccount(request,
                request.getAccountCreateRequest(),
                findStoreById(request.getStoreId()), request.getWorkShiftId(),
                role));
    }

    @Override
    public Employees createOwnerForStore(EmployeeCreateRequest request, Stores store) {

        Roles role = roleService.initRoleForOwn(request.getStoreId(), store);

        return saveAccount(request, request.getAccountCreateRequest(), store, request.getWorkShiftId(), role);
    }

    private Employees saveAccount(EmployeeCreateRequest employeeC, AccountCreateRequest request, Stores store, Integer workShiftId, Roles role) {
        Accounts account = accountService.createAccountForEmployee(request);

        WorkShifts workShifts = workShiftId !=null ?  findWorkShiftById(workShiftId) : null;

        Employees employee = mapper.toEmployeeEntity(employeeC);
        employee.setRole(role);
        employee.setStore(store);
        employee.setWorkShift(workShifts);

        employee.setAccount(account);

        return repository.save(employee);
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

    @Override
    public Employees getEmployeeForAccount(Integer accountId) {
         return repository.findEmployeesByAccount_AccountId(accountId)
                .orElseThrow();
    }

    private Employees findById(Integer id) {
        log.info("Finding employee by id {}", id);

        return repository.findById(id)
                .orElseThrow(()-> new CustomException(Error.EMPLOYEE_NOT_FOUND));
    }

    private Roles findRoleById(Integer id) {
        log.info("Finding role by id {}", id);

        return roleMapper.toRoleEntity(roleService.getById(id));
    }

    private WorkShifts findWorkShiftById(Integer id) {
        log.info("Finding work shift by id {}", id);

        return workShiftsRepository.findById(id)
                .orElseThrow(()-> new CustomException(Error.WORK_SHIFT_NOT_FOUND));
    }

    private Stores findStoreById(Integer id) {
        log.info("Finding store by id {}", id);

        return storesRepository.findById(id)
                .orElseThrow(()-> new CustomException(Error.STORE_NOT_FOUND));
    }
}