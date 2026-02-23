package com.be.ssm.controller.sale;

import com.be.ssm.dto.common.APIResponse;
import com.be.ssm.dto.request.sale.CustomerCreateRequest;
import com.be.ssm.dto.request.sale.CustomerUpdateRequest;
import com.be.ssm.dto.response.sale.CustomerResponse;
import com.be.ssm.service.sale.CustomerService;
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
@RequestMapping("/customers")
@Tag(name = "Customer Management", description = "APIs for managing customer information in system")
public class CustomerController {

    private final CustomerService customerService;

    // =========================
    // CREATE CUSTOMER
    // =========================
    @PostMapping
    @Operation(
            summary = "Create new customer",
            description = "Create a new customer with personal information",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Customer created successfully",
                            content = @Content(schema = @Schema(implementation = CustomerResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<CustomerResponse>> createCustomer(
            @RequestBody @Valid CustomerCreateRequest request,
            HttpServletRequest httpRequest) {

        CustomerResponse response = customerService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(
                        true,
                        "Customer created successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    // =========================
    // GET CUSTOMER BY ID
    // =========================
    @GetMapping("/{id}")
    @Operation(
            summary = "Get customer by id",
            description = "Retrieve customer information by customer id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer retrieved successfully",
                            content = @Content(schema = @Schema(implementation = CustomerResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Customer not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<CustomerResponse>> getCustomerById(
            @PathVariable Integer id,
            HttpServletRequest httpRequest) {

        CustomerResponse response = customerService.getById(id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Customer retrieved successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    // =========================
    // UPDATE CUSTOMER
    // =========================
    @PutMapping("/{id}")
    @Operation(
            summary = "Update customer",
            description = "Update customer information by customer id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer updated successfully",
                            content = @Content(schema = @Schema(implementation = CustomerResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Customer not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<CustomerResponse>> updateCustomer(
            @PathVariable Integer id,
            @RequestBody @Valid CustomerUpdateRequest request,
            HttpServletRequest httpRequest) {

        CustomerResponse response = customerService.update(request, id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Customer updated successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }
}