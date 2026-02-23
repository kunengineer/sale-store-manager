package com.be.ssm.controller.store;

import com.be.ssm.dto.common.APIResponse;
import com.be.ssm.dto.request.store.StoreCreateRequest;
import com.be.ssm.dto.request.store.StoreUpdateRequest;
import com.be.ssm.dto.response.store.StoreResponse;
import com.be.ssm.service.store.StoreService;
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
@RequestMapping("/stores")
@Tag(name = "Store Management", description = "APIs for managing store information in system")
public class StoreController {
    private final StoreService storeService;

    @PostMapping
    @Operation(
            summary = "Create new store",
            description = "Create a new store with basic information",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Store created successfully",
                            content = @Content(schema = @Schema(implementation = StoreResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<StoreResponse>> createStore(
            @RequestBody @Valid StoreCreateRequest request,
            HttpServletRequest httpRequest) {

        StoreResponse response = storeService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(
                        true,
                        "Store created successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get store by id",
            description = "Retrieve store information by store id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Store retrieved successfully",
                            content = @Content(schema = @Schema(implementation = StoreResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Store not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<StoreResponse>> getStoreById(
            @PathVariable Integer id,
            HttpServletRequest httpRequest) {

        StoreResponse response = storeService.getById(id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Store retrieved successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update store",
            description = "Update store information by store id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Store updated successfully",
                            content = @Content(schema = @Schema(implementation = StoreResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Store not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<StoreResponse>> updateStore(
            @PathVariable Integer id,
            @RequestBody @Valid StoreUpdateRequest request,
            HttpServletRequest httpRequest) {

        StoreResponse response = storeService.update(request, id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Store updated successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }
}
