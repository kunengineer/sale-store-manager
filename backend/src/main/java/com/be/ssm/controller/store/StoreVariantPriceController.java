package com.be.ssm.controller.store;

import com.be.ssm.dto.common.APIResponse;
import com.be.ssm.dto.request.store.StoreVariantPriceCreateRequest;
import com.be.ssm.dto.request.store.StoreVariantPriceUpdateRequest;
import com.be.ssm.dto.response.store.StoreVariantPriceResponse;
import com.be.ssm.service.store.StoreVariantPriceService;
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
@RequestMapping("/store-variant-prices")
@Tag(name = "Store Variant Price Management", description = "APIs for managing variant price per store")
public class StoreVariantPriceController {
    private final StoreVariantPriceService storeVariantPriceService;

    @PostMapping
    @Operation(
            summary = "Create store variant price",
            description = "Create a price for product variant in a specific store",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Store variant price created successfully",
                            content = @Content(schema = @Schema(implementation = StoreVariantPriceResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<StoreVariantPriceResponse>> create(
            @RequestBody @Valid StoreVariantPriceCreateRequest request,
            HttpServletRequest httpRequest) {

        StoreVariantPriceResponse response = storeVariantPriceService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(
                        true,
                        "Store variant price created successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update store variant price",
            description = "Update price of product variant for a specific store",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Store variant price updated successfully",
                            content = @Content(schema = @Schema(implementation = StoreVariantPriceResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Store variant price not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<StoreVariantPriceResponse>> update(
            @PathVariable Integer id,
            @RequestBody @Valid StoreVariantPriceUpdateRequest request,
            HttpServletRequest httpRequest) {

        StoreVariantPriceResponse response = storeVariantPriceService.update(id, request);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Store variant price updated successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }
}
