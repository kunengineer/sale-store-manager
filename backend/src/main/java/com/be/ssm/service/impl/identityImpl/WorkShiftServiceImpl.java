package com.be.ssm.service.impl.identityImpl;

import com.be.ssm.dto.request.identity.WorkShiftCreateRequest;
import com.be.ssm.dto.request.identity.WorkShiftUpdateRequest;
import com.be.ssm.dto.response.identity.WorkShiftResponse;
import com.be.ssm.entities.identity.WorkShifts;
import com.be.ssm.entities.store.Stores;
import com.be.ssm.exceptions.CustomException;
import com.be.ssm.exceptions.Error;
import com.be.ssm.mapper.identity.WorkShiftMapper;
import com.be.ssm.repository.identity.WorkShiftsRepository;
import com.be.ssm.repository.store.StoresRepository;
import com.be.ssm.service.identity.WorkShiftService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class WorkShiftServiceImpl implements WorkShiftService {

    private final WorkShiftsRepository repository;
    private final StoresRepository storesRepository;
    private final WorkShiftMapper mapper;

    @Override
    public WorkShiftResponse getById(Integer id) {
        log.info("Getting work shift by id {}", id);

        return mapper.toWorkShiftResponse(findById(id));
    }

    @Override
    public WorkShiftResponse create(WorkShiftCreateRequest request) {
        log.info("Create new work shift");
        Stores stores = storesRepository.findById(request.getStoreId())
                .orElseThrow();

        WorkShifts workShift = mapper.toWorkShiftEntity(request);
        workShift.setStore(stores);

        return mapper.toWorkShiftResponse(repository.save(workShift));
    }

    @Override
    public WorkShiftResponse update(WorkShiftUpdateRequest request, Integer id) {
        log.info("Update work shift with id {}", id);

        WorkShifts workShift = findById(id);

        mapper.updateEntityFromRequest(request, workShift);

        return mapper.toWorkShiftResponse(repository.save(workShift));
    }

    private WorkShifts findById(Integer id) {
        log.info("Finding work shift by id {}", id);

        return repository.findById(id)
                .orElseThrow(()-> new CustomException(Error.WORK_SHIFT_NOT_FOUND));
    }
}