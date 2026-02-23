package com.be.ssm.service.impl.storeImpl;

import com.be.ssm.dto.request.store.StoreTableCreateRequest;
import com.be.ssm.dto.request.store.StoreTableUpdateRequest;
import com.be.ssm.dto.response.store.StoreTableResponse;
import com.be.ssm.entities.store.StoreTables;
import com.be.ssm.entities.store.StoreZones;
import com.be.ssm.mapper.store.StoreTableMapper;
import com.be.ssm.repository.store.StoreTablesRepository;
import com.be.ssm.repository.store.StoreZonesRepository;
import com.be.ssm.service.store.StoreTableService;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class StoreTableServiceImpl implements StoreTableService {
    private final StoreTablesRepository repository;
    private final StoreZonesRepository storeZonesRepository;
    private final StoreTableMapper mapper;


    @Override
    public StoreTableResponse getById(Integer tableId) {

        return mapper.toStoreTableResponse(findById(tableId));
    }

    @Override
    public StoreTableResponse create(StoreTableCreateRequest createRequest) {
        log.info("Creating store table");

        StoreZones zone = storeZonesRepository.findById(createRequest.getStoreZoneId())
                .orElseThrow();

        isDuplicateTableCode(zone.getZoneId(), createRequest.getTableCode());

        StoreTables table = mapper.toStoreTableEntity(createRequest);
        table.setZone(zone);

        return mapper.toStoreTableResponse(repository.save(table));
    }

    @Override
    public StoreTableResponse update(StoreTableUpdateRequest updateRequest, Integer tableId) {
        log.info("Updating store table");
        StoreZones zone = storeZonesRepository.findById(updateRequest.getStoreZoneId())
                .orElseThrow();

        isDuplicateTableCode(zone.getZoneId(), updateRequest.getTableCode());

        StoreTables table = findById(tableId);
        table.setZone(zone);

        mapper.updateEntityFromRequest(updateRequest, table);

        return mapper.toStoreTableResponse(repository.save(table));
    }

    private void isDuplicateTableCode(Integer zoneId, String tableCode) {
        if (repository.existsByZoneZoneIdAndTableCode(
                zoneId,
                tableCode)) {

            throw new DuplicateRequestException(
                    String.format("Table code '%s' already exists in zone %d",
                            tableCode,
                            zoneId)
            );
        }
    }

    private StoreTables findById(Integer tableId) {
        log.info("Finding store table by id: {}", tableId);

        return repository.findById(tableId)
                .orElseThrow();
    }
}
