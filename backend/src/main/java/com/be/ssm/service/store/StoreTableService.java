package com.be.ssm.service.store;

import com.be.ssm.dto.request.store.StoreTableCreateRequest;
import com.be.ssm.dto.request.store.StoreTableUpdateRequest;
import com.be.ssm.dto.response.store.StoreTableResponse;
import org.springframework.stereotype.Service;

@Service
public interface StoreTableService {
    StoreTableResponse getById(Integer tableId);

    StoreTableResponse create(StoreTableCreateRequest createRequest);

    StoreTableResponse update(StoreTableUpdateRequest updateRequest, Integer tableId);
}
