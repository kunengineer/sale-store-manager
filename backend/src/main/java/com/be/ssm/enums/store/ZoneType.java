package com.be.ssm.enums.store;

import lombok.Getter;

@Getter
public enum ZoneType {

    SALES_FLOOR("Khu bán hàng"),
    STORAGE("Kho lưu trữ"),
    CASHIER("Quầy thu ngân"),
    LOUNGE("Khu nghỉ / Lounge");

    private final String description;

    ZoneType(String description) {
        this.description = description;
    }
}