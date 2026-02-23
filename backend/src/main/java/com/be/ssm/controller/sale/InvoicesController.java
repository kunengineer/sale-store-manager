package com.be.ssm.controller.sale;


import com.be.ssm.dto.common.APIResponse;
import com.be.ssm.dto.request.sale.InvoiceCreateRequest;
import com.be.ssm.dto.request.sale.InvoiceUpdateRequest;
import com.be.ssm.dto.response.sale.InvoiceResponse;
import com.be.ssm.service.sale.InvoicesService;
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
@RequestMapping("/invoices")
@Tag(name = "Invoice Management", description = "APIs for managing invoices in system")
public class InvoicesController {

    private final InvoicesService invoicesService;

    // =========================
    // CREATE INVOICE
    // =========================
    @PostMapping
    @Operation(
            summary = "Create new invoice",
            description = "Create a new invoice for an order",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Invoice created successfully",
                            content = @Content(schema = @Schema(implementation = InvoiceResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<InvoiceResponse>> createInvoice(
            @RequestBody @Valid InvoiceCreateRequest request,
            HttpServletRequest httpRequest) {

        InvoiceResponse response = invoicesService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(
                        true,
                        "Invoice created successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    // =========================
    // GET INVOICE BY ID
    // =========================
    @GetMapping("/{id}")
    @Operation(
            summary = "Get invoice by id",
            description = "Retrieve invoice information by invoice id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Invoice retrieved successfully",
                            content = @Content(schema = @Schema(implementation = InvoiceResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Invoice not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<InvoiceResponse>> getInvoiceById(
            @PathVariable Integer id,
            HttpServletRequest httpRequest) {

        InvoiceResponse response = invoicesService.getById(id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Invoice retrieved successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    // =========================
    // UPDATE INVOICE
    // =========================
    @PutMapping("/{id}")
    @Operation(
            summary = "Update invoice",
            description = "Update invoice information by invoice id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Invoice updated successfully",
                            content = @Content(schema = @Schema(implementation = InvoiceResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Invoice not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<InvoiceResponse>> updateInvoice(
            @PathVariable Integer id,
            @RequestBody @Valid InvoiceUpdateRequest request,
            HttpServletRequest httpRequest) {

        InvoiceResponse response = invoicesService.update(request, id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Invoice updated successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }
}