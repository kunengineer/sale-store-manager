package com.be.ssm.dto.request.store;

import com.be.ssm.enums.store.TableStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreTableUpdateRequest {
    private Integer storeZoneId;
    private String tableCode;
    private Integer seats;
    private TableStatus status;
}
