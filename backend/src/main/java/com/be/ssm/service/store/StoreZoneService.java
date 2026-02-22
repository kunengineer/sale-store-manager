package com.be.ssm.service.store;

import com.be.ssm.dto.request.store.StoreZonesCreateRequest;
import com.be.ssm.dto.request.store.StoreZonesUpdateRequest;
import com.be.ssm.dto.response.store.StoreZoneResponse;
import org.springframework.stereotype.Service;

@Service
public interface StoreZoneService {
    StoreZoneResponse getById(Integer zoneId);

    StoreZoneResponse create(StoreZonesCreateRequest createRequest);

    StoreZoneResponse update(StoreZonesUpdateRequest updateRequest, Integer zoneId);
}
