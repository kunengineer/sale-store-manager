package com.be.ssm.dto.request.store;

import lombok.Data;

@Data
public class MergeTablesRequest {
    private Integer sourceTableId;
    private Integer targetTableId;
}
