package com.be.ssm.dto.response.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreResponse {
    private Integer storeId;
    private String storeCode;
    private String storeName;
    private String address;
    private String phone;
    private String email;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
