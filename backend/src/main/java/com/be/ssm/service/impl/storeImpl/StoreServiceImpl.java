package com.be.ssm.service.impl.storeImpl;

import com.be.ssm.dto.request.store.StoreCreateRequest;
import com.be.ssm.dto.request.store.StoreUpdateRequest;
import com.be.ssm.dto.response.store.StoreResponse;
import com.be.ssm.entities.store.Stores;
import com.be.ssm.mapper.store.StoreMapper;
import com.be.ssm.repository.store.StoresRepository;
import com.be.ssm.service.store.StoreService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class StoreServiceImpl implements StoreService {

    private final StoresRepository repository;
    private final StoreMapper mapper;

    @Override
    public StoreResponse getById(Integer id) {
        log.info("Getting store by id {}", id);

        return mapper.toStoreResponse(findById(id));
    }

    @Override
    public StoreResponse create(StoreCreateRequest request) {
        log.info("Create new store");

        Stores stores = mapper.toStoreEntity(request);

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
                .orElseThrow();
    }
}
