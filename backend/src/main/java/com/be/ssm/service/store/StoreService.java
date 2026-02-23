package com.be.ssm.service.store;

import com.be.ssm.dto.request.store.StoreCreateRequest;
import com.be.ssm.dto.request.store.StoreUpdateRequest;
import com.be.ssm.dto.response.store.StoreResponse;
import org.springframework.stereotype.Service;

@Service
public interface StoreService {
    StoreResponse getById(Integer id);

    StoreResponse getByManager();

    StoreResponse create(StoreCreateRequest request);

    StoreResponse update(StoreUpdateRequest request, Integer storeId);

}
