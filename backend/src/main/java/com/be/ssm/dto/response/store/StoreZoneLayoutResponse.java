package com.be.ssm.dto.response.store;

import com.be.ssm.enums.store.ZoneType;
import lombok.Data;

import java.util.List;

@Data
public class StoreZoneLayoutResponse {
    private Integer zoneId;
    private String zoneName;
    private ZoneType zoneType;
    private List<StoreTableResponse> tables;
}
