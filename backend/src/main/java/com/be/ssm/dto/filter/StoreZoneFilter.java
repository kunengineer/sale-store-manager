package com.be.ssm.dto.filter;

import com.be.ssm.enums.store.ZoneType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class StoreZoneFilter {
    @NotNull(message = "Store id must not be null")
    @Positive(message = "Store id must be positive")
    @Schema(example = "1", description = "Store identifier")
    private Integer storeId;

    @Schema(example = "VIP Area", description = "Filter by zone name (partial match supported)")
    private String zoneName;

    @Schema(example = "DINING", description = "Filter by zone type")
    private ZoneType zoneType;

    @Schema(example = "50", description = "Filter by maximum capacity")
    private Integer capacity;

    @Schema(example = "true", description = "Filter by active status")
    private Boolean isActive;
}
