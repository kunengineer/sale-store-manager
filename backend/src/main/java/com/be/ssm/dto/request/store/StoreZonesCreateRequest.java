package com.be.ssm.dto.request.store;

import com.be.ssm.enums.store.ZoneType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class StoreZonesCreateRequest {
    @NotNull(message = "Store id must not be null")
    @Positive(message = "Store id must be positive")
    @Schema(example = "1")
    private Integer storeId;

    @NotBlank(message = "Zone name must not be blank")
    @Size(max = 100, message = "Zone name must not exceed 100 characters")
    @Schema(example = "VIP Area")
    private String zoneName;

    @NotNull(message = "Zone type must not be null")
    @Schema(example = "SALES_FLOOR")
    private ZoneType zoneType;

    @Positive(message = "Capacity must be greater than 0")
    @Schema(example = "50")
    private Integer capacity;

    @NotNull(message = "Active status must not be null")
    @Schema(example = "true")
    private Boolean isActive;
}
