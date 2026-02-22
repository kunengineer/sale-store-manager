package com.be.ssm.service.impl.storeImpl;

import com.be.ssm.dto.request.store.StoreZonesCreateRequest;
import com.be.ssm.dto.request.store.StoreZonesUpdateRequest;
import com.be.ssm.dto.response.store.StoreZoneResponse;
import com.be.ssm.entities.store.StoreZones;
import com.be.ssm.entities.store.Stores;
import com.be.ssm.mapper.store.StoreZoneMapper;
import com.be.ssm.repository.store.StoreZonesRepository;
import com.be.ssm.repository.store.StoresRepository;
import com.be.ssm.service.store.StoreZoneService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class StoreZoneServiceImpl implements StoreZoneService {
    private final StoreZonesRepository repository;
    private final StoresRepository storesRepository;
    private final StoreZoneMapper mapper;


    @Override
    public StoreZoneResponse getById(Integer zoneId) {
        return mapper.toStoreZoneResponse(findById(zoneId));
    }

    @Override
    public StoreZoneResponse create(StoreZonesCreateRequest createRequest) {
        log.info("Creating store zone");

        Stores store = storesRepository.findById(createRequest.getStoreId())
                .orElseThrow();

        StoreZones storeZones = mapper.toStoreZoneEntity(createRequest);
        storeZones.setStore(store);

        return mapper.toStoreZoneResponse(repository.save(storeZones));
    }

    @Override
    public StoreZoneResponse update(StoreZonesUpdateRequest updateRequest, Integer zoneId) {
        log.info("Updating store zone");
        Stores store = storesRepository.findById(updateRequest.getStoreId())
                .orElseThrow();

        StoreZones storeZones = findById(zoneId);
        storeZones.setStore(store);

        mapper.updateEntityFromRequest(updateRequest, storeZones);

        return mapper.toStoreZoneResponse(repository.save(storeZones));
    }

    private StoreZones findById(Integer id) {
        log.info("Finding store zone by id {}", id);

        return repository.findById(id)
                .orElseThrow();
    }
}
