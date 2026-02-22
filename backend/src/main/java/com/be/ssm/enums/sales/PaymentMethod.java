package com.be.ssm.enums.sales;

public enum PaymentMethod {
    CASH,       // Thanh toán bằng tiền mặt
    CREDIT_CARD, // Thanh toán bằng thẻ tín dụng
    BANK_TRANSFER, // Thanh toán qua chuyển khoản ngân hàng
    LOYALTY_POINTS, // Thanh toán bằng điểm thưởng
    MIXED        // Thanh toán hỗn hợp (ví dụ: một phần bằng tiền mặt và một phần bằng thẻ)
}
