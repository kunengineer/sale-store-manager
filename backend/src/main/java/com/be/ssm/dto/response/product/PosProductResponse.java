package com.be.ssm.dto.response.product;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PosProductResponse {
    private Integer productId;
    private String productName;
    private String categoryName;
    private List<PosVariantResponse> variants;
}
