package com.be.ssm.dto.response.sale;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private Integer orderId;
    private String orderNumber;
    private Integer storeId;
    private Integer customerId;
    private Integer employeeId;
    private Integer tableId;
    private String status;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal vat;
    private BigDecimal taxAmount;
    private BigDecimal grandTotal;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private List<OrderItemResponse> orderItems;
}
