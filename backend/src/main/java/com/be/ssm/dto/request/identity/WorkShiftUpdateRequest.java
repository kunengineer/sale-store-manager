package com.be.ssm.dto.request.identity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class WorkShiftUpdateRequest {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime checkinAt;
    private LocalDateTime checkoutAt;
    private BigDecimal hoursWorked;
    private String note;
}
