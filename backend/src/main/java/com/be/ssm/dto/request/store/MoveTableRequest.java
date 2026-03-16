package com.be.ssm.dto.request.store;

import lombok.Data;

@Data
public class MoveTableRequest {
    private Integer fromTableId;
    private Integer toTableId;
}
