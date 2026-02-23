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

    @NotNull(message = "Subtotal must not be null")
    @DecimalMin(value = "0.00", inclusive = true, message = "Subtotal must be >= 0")
    @Digits(integer = 13, fraction = 2)
    @Schema(example = "500000.00")
    private BigDecimal subtotal;

    @NotNull(message = "Discount amount must not be null")
    @DecimalMin(value = "0.00", inclusive = true)
    @Digits(integer = 13, fraction = 2)
    @Schema(example = "50000.00")
    private BigDecimal discountAmount;

    @DecimalMin(value = "0.00", inclusive = true)
    @Digits(integer = 13, fraction = 2)
    @Schema(example = "10.00")
    private BigDecimal vat;

    @NotNull(message = "Tax amount must not be null")
    @DecimalMin(value = "0.00", inclusive = true)
    @Digits(integer = 13, fraction = 2)
    @Schema(example = "45000.00")
    private BigDecimal taxAmount;

    @NotNull(message = "Grand total must not be null")
    @DecimalMin(value = "0.00", inclusive = true)
    @Digits(integer = 13, fraction = 2)
    @Schema(example = "495000.00")
    private BigDecimal grandTotal;

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
