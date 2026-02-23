package com.be.ssm.dto.request.sale;

import com.be.ssm.enums.sales.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderUpdateRequest {
    @NotNull(message = "Order status must not be null")
    @Schema(example = "PENDING")
    private OrderStatus status;

    @DecimalMin(value = "0.00", inclusive = true)
    @Digits(integer = 13, fraction = 2)
    @Schema(example = "10.00")
    private BigDecimal vat;

    @Size(max = 1000)
    @Schema(example = "Customer requests less ice")
    private String note;

    @Schema(example = "1")
    private Integer customerId;

    @NotNull(message = "Table id must not be null")
    @Positive(message = "Table id must be positive")
    @Schema(example = "5")
    private Integer tableId;
}
