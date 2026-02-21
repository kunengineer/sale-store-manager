package com.be.ssm.dto.response.store;

import com.be.ssm.enums.store.TableStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreTableResponse {
    private Integer tableId;
    private String tableCode;
    private Integer seats;
    private TableStatus status;
}
