package com.be.ssm.controller.product;


import com.be.ssm.dto.common.APIResponse;
import com.be.ssm.dto.common.PageDTO;
import com.be.ssm.dto.filter.ProductFilter;
import com.be.ssm.dto.request.product.ProductCreateRequest;
import com.be.ssm.dto.request.product.ProductUpdateRequest;
import com.be.ssm.dto.response.product.PosProductResponse;
import com.be.ssm.dto.response.product.ProductResponse;
import com.be.ssm.service.product.ProductService;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @Operation(
            summary = "Create new product",
            description = "Create a new product",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Product created successfully",
                            content = @Content(schema = @Schema(implementation = ProductResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<ProductResponse>> createProduct(
            @RequestBody @Valid ProductCreateRequest request,
            HttpServletRequest httpRequest) {

        ProductResponse response = productService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(
                        true,
                        "Product created successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get product by id",
            description = "Retrieve product information by product id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product retrieved successfully",
                            content = @Content(schema = @Schema(implementation = ProductResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<ProductResponse>> getProductById(
            @PathVariable Integer id,
            HttpServletRequest httpRequest) {

        ProductResponse response = productService.getById(id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Product retrieved successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update product",
            description = "Update product information by product id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product updated successfully",
                            content = @Content(schema = @Schema(implementation = ProductResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Product not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<ProductResponse>> updateProduct(
            @PathVariable Integer id,
            @RequestBody @Valid ProductUpdateRequest request,
            HttpServletRequest httpRequest) {

        ProductResponse response = productService.update(request, id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Product updated successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    @GetMapping("/filter")
    @Operation(
            summary = "Filter & get products",
            description = "Retrieve list of products with filter and pagination"
    )
    public ResponseEntity<APIResponse<PageDTO<ProductResponse>>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @ModelAttribute ProductFilter filter,
            HttpServletRequest httpRequest
    ) {

        PageDTO<ProductResponse> response = productService.getAll(page, size, filter);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Products retrieved successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                )
        );
    }

    @GetMapping("/pos")
    @Operation(
            summary = "Get products for POS",
            description = "Retrieve active products with variants and store price for POS",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Products retrieved successfully",
                            content = @Content(schema = @Schema(implementation = PosProductResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid store id"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<List<PosProductResponse>>> getProductsForPos(
            @RequestParam Integer storeId,
            HttpServletRequest httpRequest
    ) {

        List<PosProductResponse> response =
                productService.getProductsForPos(storeId);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "POS products retrieved successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                )
        );
    }
}
