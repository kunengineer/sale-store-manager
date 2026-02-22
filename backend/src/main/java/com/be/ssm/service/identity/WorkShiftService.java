package com.be.ssm.service.identity;

import com.be.ssm.dto.request.identity.WorkShiftCreateRequest;
import com.be.ssm.dto.request.identity.WorkShiftUpdateRequest;
import com.be.ssm.dto.response.identity.WorkShiftResponse;
import org.springframework.stereotype.Service;

@Service
public interface WorkShiftService {
    WorkShiftResponse getById(Integer id);

    WorkShiftResponse create(WorkShiftCreateRequest request);

    WorkShiftResponse update(WorkShiftUpdateRequest request, Integer workShiftId);
}
