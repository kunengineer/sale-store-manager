package com.be.ssm.service.impl.storeImpl;

import com.be.ssm.dto.request.store.RegisterNewStore;
import com.be.ssm.dto.request.store.StoreCreateRequest;
import com.be.ssm.dto.request.store.StoreUpdateRequest;
import com.be.ssm.dto.response.store.StoreResponse;
import com.be.ssm.entities.account.Accounts;
import com.be.ssm.entities.identity.Employees;
import com.be.ssm.entities.store.Stores;
import com.be.ssm.exceptions.CustomException;
import com.be.ssm.exceptions.Error;
import com.be.ssm.mapper.store.StoreMapper;
import com.be.ssm.repository.account.AccountRepository;
import com.be.ssm.repository.store.StoresRepository;
import com.be.ssm.service.identity.EmployeeService;
import com.be.ssm.service.impl.accountImpl.OurUserDetailsService;
import com.be.ssm.service.store.StoreService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class StoreServiceImpl implements StoreService {
    private final OurUserDetailsService userDetailsService;
    private final EmployeeService employeeService;

    private final StoresRepository repository;
    private final StoreMapper mapper;

    @Override
    public StoreResponse getById(Integer id) {
        log.info("Getting store by id {}", id);

        return mapper.toStoreResponse(findById(id));
    }

    @Override
    public List<StoreResponse> getByManager() {

        Accounts manager = userDetailsService.getAccountAuth();
        List<Stores> store = repository.findAllByManager(manager);

        return mapper.toStoreResponseList(store);
    }

    @Override
    public StoreResponse create(StoreCreateRequest request) {
        Employees employee = employeeService.getEmployeeForAccount(userDetailsService.getAccountAuth().getAccountId());

        Stores stores = mapper.toStoreEntity(request);
        stores.setManager(employee);

        return mapper.toStoreResponse(repository.save(stores));
    }

    @Override
    public StoreResponse registerNewStore(RegisterNewStore request) {
        log.info("Create new store");
        Stores stores = repository.save(mapper.toStoreEntity(request));

        request.getEmployee().setStoreId(stores.getStoreId());
        Employees employees = employeeService.createOwnerForStore(request.getEmployee());

        stores.setManager(employees);

        return mapper.toStoreResponse(repository.save(stores));
    }

    @Override
    public StoreResponse update(StoreUpdateRequest request, Integer id) {
        log.info("Update store");

        Stores stores = findById(id);

        mapper.updateEntityFromRequest(request, stores);

        return mapper.toStoreResponse(repository.save(stores));
    }

    private Stores findById(Integer id){
        log.info("Finding store by id {}", id);

        return repository.findById(id)
                .orElseThrow(()-> new CustomException(Error.STORE_NOT_FOUND));
    }
}
