package com.be.ssm.dto.request.store;

import com.be.ssm.enums.store.ZoneType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreZonesCreateRequest {
    private Integer storeId;
    private String zoneName;
    private ZoneType zoneType;
    private Integer capacity;
    private Boolean isActive;
}
