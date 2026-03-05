package com.be.ssm.dto.request.store;

import com.be.ssm.enums.store.TableStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class StoreTableUpdateRequest {
    private Integer storeZoneId;
    private String tableCode;
    private Integer seats;
    private TableStatus status;
    private Boolean isActive;
}
