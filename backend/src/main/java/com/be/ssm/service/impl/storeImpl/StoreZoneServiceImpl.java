package com.be.ssm.service.impl.storeImpl;

import com.be.ssm.dto.common.PageDTO;
import com.be.ssm.dto.filter.StoreZoneFilter;
import com.be.ssm.dto.request.store.StoreZonesCreateRequest;
import com.be.ssm.dto.request.store.StoreZonesUpdateRequest;
import com.be.ssm.dto.response.store.StoreZoneLayoutResponse;
import com.be.ssm.dto.response.store.StoreZoneResponse;
import com.be.ssm.entities.store.StoreTables;
import com.be.ssm.entities.store.StoreZones;
import com.be.ssm.entities.store.Stores;
import com.be.ssm.exceptions.CustomException;
import com.be.ssm.exceptions.Error;
import com.be.ssm.mapper.store.StoreZoneMapper;
import com.be.ssm.repository.store.StoreTablesRepository;
import com.be.ssm.repository.store.StoreZonesRepository;
import com.be.ssm.repository.store.StoresRepository;
import com.be.ssm.service.store.StoreZoneService;
import com.be.ssm.specification.StoreZoneSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public PageDTO<StoreZoneResponse> filter(int page, int size, StoreZoneFilter filter) {
        log.info("Filtering store zones");

        Specification<StoreZones> spec = StoreZoneSpecification.filter(filter);
        Pageable pageable = PageRequest.of(page-1, size);

        return mapper.toPageDTO(repository.findAll(spec, pageable));
    }

    private StoreZones findById(Integer id) {
        log.info("Finding store zone by id {}", id);

        return repository.findById(id)
                .orElseThrow(()-> new CustomException(Error.STORE_ZONE_NOT_FOUND));
    }

    @Override
    public List<StoreZoneLayoutResponse> getLayoutByStore(Integer storeId) {

        List<StoreZones> zones = repository.findByStoreStoreId(storeId);

        return zones.stream()
                .map(mapper::toStoreZoneLayoutResponse)
                .toList();
    }
}
