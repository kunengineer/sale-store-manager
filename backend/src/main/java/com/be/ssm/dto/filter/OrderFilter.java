package com.be.ssm.dto.filter;

import com.be.ssm.enums.sales.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderFilter {
    private String orderNumber;
    private Integer storeTableId;
    private Integer customerId;
    private OrderStatus status;
    private Integer storeId;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private BigDecimal minTotal;
    private BigDecimal maxTotal;
}