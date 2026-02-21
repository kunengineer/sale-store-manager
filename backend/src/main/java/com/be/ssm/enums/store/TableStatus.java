package com.be.ssm.enums.store;

import lombok.Getter;

@Getter
public enum TableStatus {
    AVAILABLE("Bàn trống"),
    OCCUPIED("Đang sử dụng"),
    RESERVED("Đã đặt trước"),
    MAINTENANCE("Đang bảo trì");

    private final String description;

    TableStatus(String description) {
        this.description = description;
    }
}
