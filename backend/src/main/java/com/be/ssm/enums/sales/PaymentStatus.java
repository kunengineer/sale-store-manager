package com.be.ssm.enums.sales;

public enum PaymentStatus {
    PENDING,   // Đang chờ xử lý
    COMPLETE, // Đã hoàn thành
    FAILED,    // Giao dịch thất bại
    REFUNDED   // Đã hoàn tiền
}
