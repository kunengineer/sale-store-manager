package com.be.ssm.controller.product;

import com.be.ssm.dto.common.APIResponse;
import com.be.ssm.dto.common.PageDTO;
import com.be.ssm.dto.filter.CategoryFilter;
import com.be.ssm.dto.request.product.CategoryCreateRequest;
import com.be.ssm.dto.request.product.CategoryUpdateRequest;
import com.be.ssm.dto.response.product.CategoriesResponse;
import com.be.ssm.service.product.CategoriesService;
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
@RequestMapping("/categories")
@Tag(name = "Category Management", description = "APIs for managing product categories")
public class CategoriesController {
    private final CategoriesService categoriesService;

    @PostMapping
    @Operation(
            summary = "Create new category",
            description = "Create a new product category",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Category created successfully",
                            content = @Content(schema = @Schema(implementation = CategoriesResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<CategoriesResponse>> createCategory(
            @RequestBody @Valid CategoryCreateRequest request,
            HttpServletRequest httpRequest) {

        CategoriesResponse response = categoriesService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(
                        true,
                        "Category created successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get category by id",
            description = "Retrieve category information by category id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category retrieved successfully",
                            content = @Content(schema = @Schema(implementation = CategoriesResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Category not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<CategoriesResponse>> getCategoryById(
            @PathVariable Integer id,
            HttpServletRequest httpRequest) {

        CategoriesResponse response = categoriesService.getById(id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Category retrieved successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update category",
            description = "Update category information by category id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category updated successfully",
                            content = @Content(schema = @Schema(implementation = CategoriesResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Category not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<CategoriesResponse>> updateCategory(
            @PathVariable Integer id,
            @RequestBody @Valid CategoryUpdateRequest request,
            HttpServletRequest httpRequest) {

        CategoriesResponse response = categoriesService.update(request, id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Category updated successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    @GetMapping("/stores/{storeId}/categories")
    public ResponseEntity<PageDTO<CategoriesResponse>> getAllCategories(
            @PathVariable Integer storeId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,

            @RequestParam(required = false) Integer parentId,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) Boolean isActive
    ) {

        CategoryFilter filter = new CategoryFilter();
        filter.setParentId(parentId);
        filter.setCategoryName(categoryName);
        filter.setIsActive(isActive);
        filter.setStoreId(storeId);

        PageDTO<CategoriesResponse> result =
                categoriesService.getAllCategories(page, size, filter);

        return ResponseEntity.ok(result);
    }
}
