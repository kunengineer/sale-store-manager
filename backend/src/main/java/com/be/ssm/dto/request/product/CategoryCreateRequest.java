package com.be.ssm.dto.request.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryCreateRequest {
    @NotBlank(message = "Category name must not be blank")
    @Schema(example = "Coffee")
    private String categoryName;

    @PositiveOrZero(message = "Sort order must be >= 0")
    @Schema(example = "1")
    private Integer sortOrder;

    @NotNull(message = "Active status must not be null")
    @Schema(example = "true")
    private Boolean isActive;

    @Schema(example = "https://cdn.example.com/images/categories/coffee.png")
    private String imageUrl;
}
