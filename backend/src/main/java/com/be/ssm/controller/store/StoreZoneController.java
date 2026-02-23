package com.be.ssm.controller.store;

import com.be.ssm.dto.common.APIResponse;
import com.be.ssm.dto.request.store.StoreZonesCreateRequest;
import com.be.ssm.dto.request.store.StoreZonesUpdateRequest;
import com.be.ssm.dto.response.store.StoreZoneResponse;
import com.be.ssm.service.store.StoreZoneService;
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
@RequestMapping("/store-zones")
@Tag(name = "Store Zone Management", description = "APIs for managing store zones in system")
public class StoreZoneController {
    private final StoreZoneService storeZoneService;

    // =========================
    // CREATE STORE ZONE
    // =========================
    @PostMapping
    @Operation(
            summary = "Create new store zone",
            description = "Create a new zone for a specific store",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Store zone created successfully",
                            content = @Content(schema = @Schema(implementation = StoreZoneResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<StoreZoneResponse>> createStoreZone(
            @RequestBody @Valid StoreZonesCreateRequest request,
            HttpServletRequest httpRequest) {

        StoreZoneResponse response = storeZoneService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(
                        true,
                        "Store zone created successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    // =========================
    // GET STORE ZONE BY ID
    // =========================
    @GetMapping("/{id}")
    @Operation(
            summary = "Get store zone by id",
            description = "Retrieve store zone information by zone id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Store zone retrieved successfully",
                            content = @Content(schema = @Schema(implementation = StoreZoneResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Store zone not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<StoreZoneResponse>> getStoreZoneById(
            @PathVariable Integer id,
            HttpServletRequest httpRequest) {

        StoreZoneResponse response = storeZoneService.getById(id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Store zone retrieved successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    // =========================
    // UPDATE STORE ZONE
    // =========================
    @PutMapping("/{id}")
    @Operation(
            summary = "Update store zone",
            description = "Update store zone information by zone id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Store zone updated successfully",
                            content = @Content(schema = @Schema(implementation = StoreZoneResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Store zone not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<StoreZoneResponse>> updateStoreZone(
            @PathVariable Integer id,
            @RequestBody @Valid StoreZonesUpdateRequest request,
            HttpServletRequest httpRequest) {

        StoreZoneResponse response = storeZoneService.update(request, id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Store zone updated successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }
}
