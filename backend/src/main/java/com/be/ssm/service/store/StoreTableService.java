package com.be.ssm.service.store;

import com.be.ssm.dto.common.PageDTO;
import com.be.ssm.dto.filter.StoreTableFilter;
import com.be.ssm.dto.request.store.MergeTablesRequest;
import com.be.ssm.dto.request.store.MoveTableRequest;
import com.be.ssm.dto.request.store.StoreTableCreateRequest;
import com.be.ssm.dto.request.store.StoreTableUpdateRequest;
import com.be.ssm.dto.response.store.StoreTableResponse;
import com.be.ssm.dto.response.store.StoreZoneLayoutResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StoreTableService {
    StoreTableResponse getById(Integer tableId);

    StoreTableResponse create(StoreTableCreateRequest createRequest);

    StoreTableResponse update(StoreTableUpdateRequest updateRequest, Integer tableId);

    PageDTO<StoreTableResponse> filter(int page, int size, StoreTableFilter filter);

    StoreTableResponse moveTable(Integer tableId, MoveTableRequest request);

    void mergeTables(MergeTablesRequest request);

    void unmergeTable(Integer tableId);
}
