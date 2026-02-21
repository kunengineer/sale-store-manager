package com.be.ssm.dto.response.store;

import com.be.ssm.enums.store.ZoneType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreZoneResponse {
    private Integer zoneId;
    private String zoneName;
    private ZoneType zoneType;
    private Integer capacity;
    private Boolean isActive;
}
