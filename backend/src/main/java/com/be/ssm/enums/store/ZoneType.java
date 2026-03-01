package com.be.ssm.enums.store;

import lombok.Getter;

@Getter
public enum ZoneType {

    FLOOR("Khu tầng / khu vực phục vụ chung"),      // Tầng trệt, Lầu 1, Lầu 2
    ROOM("Phòng riêng / VIP / máy lạnh"),           // Phòng VIP, phòng lạnh
    OUTDOOR("Khu ngoài trời / sân vườn / ban công"),// Sân vườn, ban công
    BAR("Quầy bar / pha chế"),                      // Quầy pha chế
    CASHIER("Quầy thu ngân"),                       // Quầy tính tiền
    STORAGE("Kho / hậu cần");                       // Kho nguyên liệu

    private final String description;

    ZoneType(String description) {
        this.description = description;
    }
}