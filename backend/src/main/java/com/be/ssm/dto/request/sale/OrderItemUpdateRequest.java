package com.be.ssm.dto.request.sale;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemUpdateRequest {
    @NotNull(message = "Order id must not be null")
    @Positive(message = "Order id must be positive")
    @Schema(example = "10")
    private Integer orderId;

    @NotNull(message = "Product variant id must not be null")
    @Positive(message = "Product variant id must be positive")
    @Schema(example = "5")
    private Integer productVariantId;

    @NotNull(message = "Quantity must not be null")
    @Positive(message = "Quantity must be greater than 0")
    @Schema(example = "2")
    private Integer quantity;

    @NotNull(message = "Unit price must not be null")
    @DecimalMin(value = "0.00", inclusive = true)
    @Digits(integer = 13, fraction = 2)
    @Schema(example = "120000.00")
    private BigDecimal unitPrice;

    @NotNull(message = "Discount percentage must not be null")
    @DecimalMin(value = "0.00", inclusive = true)
    @DecimalMax(value = "100.00", inclusive = true)
    @Digits(integer = 3, fraction = 2)
    @Schema(example = "10.00")
    private BigDecimal discountPct;

    @NotNull(message = "Discount amount must not be null")
    @DecimalMin(value = "0.00", inclusive = true)
    @Digits(integer = 13, fraction = 2)
    @Schema(example = "24000.00")
    private BigDecimal discountAmt;

    @NotNull(message = "Line total must not be null")
    @DecimalMin(value = "0.00", inclusive = true)
    @Digits(integer = 13, fraction = 2)
    @Schema(example = "216000.00")
    private BigDecimal lineTotal;

    @Size(max = 1000, message = "Note must not exceed 1000 characters")
    @Schema(example = "Less sugar")
    private String note;
}
