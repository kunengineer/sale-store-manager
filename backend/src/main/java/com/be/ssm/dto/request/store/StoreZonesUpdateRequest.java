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
public class StoreZonesUpdateRequest {
    private Integer storeId;
    private String zoneName;
    private ZoneType zoneType;
    private Integer capacity;
    private Boolean isActive;
}
