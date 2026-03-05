package com.be.ssm.enums.store;

import lombok.Getter;

@Getter
public enum TableStatus {
    INACTIVE("Tạm khóa"),
    AVAILABLE("Bàn trống"),
    OCCUPIED("Đang sử dụng"),
    RESERVED("Đã đặt trước"),
    MAINTENANCE("Đang bảo trì"),
    MERGED("Đã gộp");

    private final String description;

    TableStatus(String description) {
        this.description = description;
    }
}
