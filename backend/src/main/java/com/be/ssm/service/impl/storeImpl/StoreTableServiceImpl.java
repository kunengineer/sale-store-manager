package com.be.ssm.service.impl.storeImpl;

import com.be.ssm.dto.common.PageDTO;
import com.be.ssm.dto.filter.StoreTableFilter;
import com.be.ssm.dto.request.store.MergeTablesRequest;
import com.be.ssm.dto.request.store.MoveTableRequest;
import com.be.ssm.dto.request.store.StoreTableCreateRequest;
import com.be.ssm.dto.request.store.StoreTableUpdateRequest;
import com.be.ssm.dto.response.store.StoreTableResponse;
import com.be.ssm.dto.response.store.StoreZoneLayoutResponse;
import com.be.ssm.entities.store.StoreTables;
import com.be.ssm.entities.store.StoreZones;
import com.be.ssm.enums.store.TableStatus;
import com.be.ssm.exceptions.CustomException;
import com.be.ssm.exceptions.Error;
import com.be.ssm.mapper.store.StoreTableMapper;
import com.be.ssm.mapper.store.StoreZoneMapper;
import com.be.ssm.repository.store.StoreTablesRepository;
import com.be.ssm.repository.store.StoreZonesRepository;
import com.be.ssm.service.sale.OrderService;
import com.be.ssm.service.store.StoreTableService;
import com.be.ssm.specification.StoreTableSpecification;
import com.sun.jdi.request.DuplicateRequestException;
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
public class StoreTableServiceImpl implements StoreTableService {
    private final StoreTablesRepository repository;
    private final StoreZonesRepository storeZonesRepository;
    private final StoreTableMapper mapper;

    private final OrderService orderService;


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
        table.setIsActive(false);
        table.setStatus(TableStatus.MAINTENANCE);

        return mapper.toStoreTableResponse(repository.save(table));
    }

    @Override
    public StoreTableResponse update(StoreTableUpdateRequest updateRequest, Integer tableId) {
        log.info("Updating store table");
        StoreZones zone = storeZonesRepository.findById(updateRequest.getStoreZoneId())
                .orElseThrow();

        isDuplicateTableCodeCheckForUpdate(tableId, zone.getZoneId(), updateRequest.getTableCode());

        StoreTables table = findById(tableId);
        table.setZone(zone);

        mapper.updateEntityFromRequest(updateRequest, table);

        return mapper.toStoreTableResponse(repository.save(table));
    }

    @Override
    public PageDTO<StoreTableResponse> filter(int page, int size, StoreTableFilter filter) {
        log.info("Filtering store table");

        Specification<StoreTables> spec = StoreTableSpecification.filter(filter);
        Pageable pageable = PageRequest.of(page -1, size);

        return mapper.toPageDTO(repository.findAll(spec, pageable));
    }

    @Override
    public StoreTables getTableAvailable(Integer tableId) {
        return repository.findStoreTablesByTableIdAndStatusEquals(tableId, TableStatus.AVAILABLE)
                .orElseThrow(()-> new CustomException(Error.STORE_TABLE_UNAVAILABLE));
    }

    @Override
    public StoreTableResponse moveTable(MoveTableRequest request) {

        StoreTables fromTable = findById(request.getFromTableId());
        StoreTables toTable = findById(request.getToTableId());
        if (toTable.getStatus() != TableStatus.AVAILABLE) {
            throw new CustomException(Error.STORE_TABLE_UNAVAILABLE);
        }

        orderService.moveOrder(fromTable.getTableId(), toTable);
        fromTable.setStatus(TableStatus.AVAILABLE);
        toTable.setStatus(TableStatus.OCCUPIED);

        repository.save(fromTable);

        return mapper.toStoreTableResponse(repository.save(toTable));
    }

    @Override
    public void mergeTables(MergeTablesRequest request) {
        StoreTables source = findById(request.getSourceTableId());
        StoreTables target = findById(request.getTargetTableId());

        if (target.getStatus() != TableStatus.OCCUPIED
                ||  source.getStatus() != TableStatus.OCCUPIED) {
            throw new CustomException(Error.TABLE_AVAILABLE);
        }

        source.setStatus(TableStatus.MERGED);
        source.setMergedIntoTableId(target.getTableId());
        orderService.mergeOrder(source.getTableId(), target.getTableId());

        repository.save(source);
    }

    @Override
    public void unmergeTable(Integer tableId) {
        StoreTables table = findById(tableId);

        if (table.getStatus() != TableStatus.MERGED) {
            throw new CustomException(Error.TABLE_NOT_MERGED);
        }

        table.setStatus(TableStatus.AVAILABLE);
        table.setMergedIntoTableId(null);

        repository.save(table);
    }

    private void isDuplicateTableCode(Integer zoneId, String tableCode) {

        if (repository.existsByZoneZoneIdAndTableCode(
                zoneId,
                tableCode) == 1) {

            throw new DuplicateRequestException(
                    String.format("Table code '%s' already exists in zone %d",
                            tableCode,
                            zoneId)
            );
        }
    }

    private void isDuplicateTableCodeCheckForUpdate(Integer tableId, Integer zoneId, String tableCode) {
        if(repository.existsTableCodeForUpdate(tableId, zoneId, tableCode) == 1){
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
                .orElseThrow(()-> new CustomException(Error.STORE_TABLE_NOT_FOUND));
    }
}
