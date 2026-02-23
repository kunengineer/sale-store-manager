package com.be.ssm.dto.request.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductCreateRequest {
    @NotNull(message = "Category id must not be null")
    @Positive(message = "Category id must be positive")
    @Schema(example = "1")
    private Integer categoryId;

    @NotBlank(message = "Product code must not be blank")
    @Size(max = 50)
    @Schema(example = "CF001")
    private String productCode;

    @NotBlank(message = "Product name must not be blank")
    @Size(max = 200)
    @Schema(example = "Americano")
    private String productName;

    @Schema(example = "Classic black coffee without milk")
    private String description;

    @NotBlank(message = "Unit must not be blank")
    @Schema(example = "cup")
    private String unit;

    @NotNull
    @DecimalMin(value = "0.0")
    @Schema(example = "45000")
    private BigDecimal basePrice;

    @NotNull
    @DecimalMin(value = "0.0")
    @Schema(example = "30000")
    private BigDecimal costPrice;

    @Schema(example = "[\"https://cdn.app.com/coffee1.png\",\"https://cdn.app.com/coffee2.png\"]")
    private String images;

    @NotNull
    @Schema(example = "true")
    private Boolean isActive;
}
