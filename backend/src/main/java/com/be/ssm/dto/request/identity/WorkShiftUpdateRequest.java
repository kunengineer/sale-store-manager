package com.be.ssm.dto.request.identity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class WorkShiftUpdateRequest {
    @Schema(example = "2026-02-23T08:00:00")
    private LocalDateTime startTime;

    @Schema(example = "2026-02-23T17:00:00")
    private LocalDateTime endTime;

    @Schema(example = "2026-02-23T08:10:00")
    private LocalDateTime checkinAt;

    @Schema(example = "2026-02-23T17:05:00")
    private LocalDateTime checkoutAt;

    @DecimalMin("0.0")
    @Schema(example = "7.5")
    private BigDecimal hoursWorked;

    @Schema(example = "Updated shift note")
    private String note;
}
