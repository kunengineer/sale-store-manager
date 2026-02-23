package com.be.ssm.dto.filter;

import com.be.ssm.enums.store.TableStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StoreTableFilter {
    @Schema(example = "10", description = "Zone identifier to filter tables by zone")

    private Integer zoneId;

    @Schema(example = "1", description = "Store identifier to filter tables by store")
    private Integer storeId;

    @Schema(example = "T01", description = "Filter by table code (partial match supported)")
    private String tableCode;

    @Schema(example = "AVAILABLE", description = "Filter by active status")
    private TableStatus status;
}
