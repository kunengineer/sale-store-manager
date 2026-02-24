package com.be.ssm.dto.request.store;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreProductPriceCreateRequest {
    @NotNull(message = "Store id must not be null")
    private Integer storeId;

    @NotNull(message = "Product id must not be null")
    private Integer productId;

    @NotNull(message = "Price must not be null")
    private BigDecimal price;

    private Boolean isActive;
}
