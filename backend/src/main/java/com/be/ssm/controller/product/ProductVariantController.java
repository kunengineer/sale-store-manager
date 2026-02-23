package com.be.ssm.controller.product;

import com.be.ssm.dto.common.APIResponse;
import com.be.ssm.dto.request.product.ProductVariantCreateRequest;
import com.be.ssm.dto.request.product.ProductVariantUpdateRequest;
import com.be.ssm.dto.response.product.ProductVariantResponse;
import com.be.ssm.service.product.ProductVariantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product-variants")
@Tag(name = "Product Variant Management", description = "APIs for managing product variants")
public class ProductVariantController {
    private final ProductVariantService productVariantService;

    @PostMapping
    public ResponseEntity<APIResponse<ProductVariantResponse>> create(
            @RequestBody @Valid ProductVariantCreateRequest request,
            HttpServletRequest httpRequest) {

        ProductVariantResponse response = productVariantService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(
                        true,
                        "Product variant created successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<ProductVariantResponse>> getById(
            @PathVariable Integer id,
            HttpServletRequest httpRequest) {

        ProductVariantResponse response = productVariantService.getById(id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Product variant retrieved successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<ProductVariantResponse>> update(
            @PathVariable Integer id,
            @RequestBody @Valid ProductVariantUpdateRequest request,
            HttpServletRequest httpRequest) {

        ProductVariantResponse response = productVariantService.update(request, id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Product variant updated successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }
}
