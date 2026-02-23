package com.be.ssm.dto.request.identity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class WorkShiftCreateRequest {
    @NotNull(message = "Store id must not be null")
    @Positive(message = "Store id must be positive")
    @Schema(example = "1")
    private Integer storeId;

    @NotNull(message = "Shift date must not be null")
    @Schema(example = "2026-02-23T00:00:00")
    private LocalDateTime shiftDate;

    @NotNull(message = "Start time must not be null")
    @Schema(example = "2026-02-23T08:00:00")
    private LocalDateTime startTime;

    @Schema(example = "2026-02-23T17:00:00")
    private LocalDateTime endTime;

    @Schema(example = "2026-02-23T08:05:00")
    private LocalDateTime checkinAt;

    @Schema(example = "2026-02-23T17:02:00")
    private LocalDateTime checkoutAt;

    @DecimalMin("0.0")
    @Schema(example = "8.0")
    private BigDecimal hoursWorked;

    @Schema(example = "Morning shift")
    private String note;
}
