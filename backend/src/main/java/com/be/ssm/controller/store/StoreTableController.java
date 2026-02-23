package com.be.ssm.controller.store;


import com.be.ssm.dto.common.APIResponse;
import com.be.ssm.dto.common.PageDTO;
import com.be.ssm.dto.filter.StoreTableFilter;
import com.be.ssm.dto.request.store.StoreTableCreateRequest;
import com.be.ssm.dto.request.store.StoreTableUpdateRequest;
import com.be.ssm.dto.response.store.StoreTableResponse;
import com.be.ssm.service.store.StoreTableService;
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
@RequestMapping("/store-tables")
@Tag(name = "Store Table Management", description = "APIs for managing store tables in system")
public class StoreTableController {

    private final StoreTableService storeTableService;

    // =========================
    // CREATE STORE TABLE
    // =========================
    @PostMapping
    @Operation(
            summary = "Create new store table",
            description = "Create a new table in a specific store zone",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Store table created successfully",
                            content = @Content(schema = @Schema(implementation = StoreTableResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<StoreTableResponse>> createStoreTable(
            @RequestBody @Valid StoreTableCreateRequest request,
            HttpServletRequest httpRequest) {

        StoreTableResponse response = storeTableService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(
                        true,
                        "Store table created successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    // =========================
    // GET STORE TABLE BY ID
    // =========================
    @GetMapping("/{id}")
    @Operation(
            summary = "Get store table by id",
            description = "Retrieve store table information by table id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Store table retrieved successfully",
                            content = @Content(schema = @Schema(implementation = StoreTableResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Store table not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<StoreTableResponse>> getStoreTableById(
            @PathVariable Integer id,
            HttpServletRequest httpRequest) {

        StoreTableResponse response = storeTableService.getById(id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Store table retrieved successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    // =========================
    // UPDATE STORE TABLE
    // =========================
    @PutMapping("/{id}")
    @Operation(
            summary = "Update store table",
            description = "Update store table information by table id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Store table updated successfully",
                            content = @Content(schema = @Schema(implementation = StoreTableResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Store table not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<StoreTableResponse>> updateStoreTable(
            @PathVariable Integer id,
            @RequestBody @Valid StoreTableUpdateRequest request,
            HttpServletRequest httpRequest) {

        StoreTableResponse response = storeTableService.update(request, id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Store table updated successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    @GetMapping("/filter")
    @Operation(
            summary = "Filter & get store tables",
            description = "Retrieve list of store tables with filter and pagination"
    )
    public ResponseEntity<APIResponse<PageDTO<StoreTableResponse>>> filter(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @ModelAttribute StoreTableFilter filter,
            HttpServletRequest httpRequest
    ) {

        PageDTO<StoreTableResponse> response =
                storeTableService.filter(page, size, filter);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Store tables retrieved successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                )
        );
    }
}