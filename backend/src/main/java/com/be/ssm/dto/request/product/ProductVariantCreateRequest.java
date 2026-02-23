package com.be.ssm.dto.request.product;

import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Builder
@Data
public class ProductVariantCreateRequest {
    @NotNull(message = "Product id must not be null")
    @Positive(message = "Product id must be positive")
    @Schema(example = "1")
    private Integer productId;

    @NotBlank(message = "Variant name must not be blank")
    @Schema(example = "Size M")
    private String variantName;

    @NotBlank(message = "SKU must not be blank")
    @Schema(example = "CF001-M")
    private String sku;

    @Schema(example = "8938505974192")
    private String barcode;

    @Schema(example = "{\"size\":\"M\",\"ice\":\"50%\"}")
    private String attributes;

    @NotNull
    @DecimalMin("0.0")
    @Schema(example = "49000")
    private BigDecimal price;

    @DecimalMin("0.0")
    @Schema(example = "30000")
    private BigDecimal costPrice;

    private Integer weightGram;

    @NotNull
    @Schema(example = "true")
    private Boolean isActive;
}
