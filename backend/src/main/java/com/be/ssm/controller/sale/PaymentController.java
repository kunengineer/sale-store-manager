package com.be.ssm.controller.sale;

import com.be.ssm.dto.common.APIResponse;
import com.be.ssm.dto.request.sale.PaymentCreateRequest;
import com.be.ssm.dto.request.sale.PaymentUpdateRequest;
import com.be.ssm.dto.response.sale.PaymentResponse;
import com.be.ssm.service.sale.PaymentService;
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
@RequestMapping("/payments")
@Tag(name = "Payment Management", description = "APIs for managing payments in system")
public class PaymentController {

    private final PaymentService paymentService;

    // =========================
    // CREATE PAYMENT
    // =========================
    @PostMapping
    @Operation(
            summary = "Create new payment",
            description = "Create a new payment for an invoice",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Payment created successfully",
                            content = @Content(schema = @Schema(implementation = PaymentResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<PaymentResponse>> createPayment(
            @RequestBody @Valid PaymentCreateRequest request,
            HttpServletRequest httpRequest) {

        PaymentResponse response = paymentService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(
                        true,
                        "Payment created successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    // =========================
    // GET PAYMENT BY ID
    // =========================
    @GetMapping("/{id}")
    @Operation(
            summary = "Get payment by id",
            description = "Retrieve payment information by payment id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payment retrieved successfully",
                            content = @Content(schema = @Schema(implementation = PaymentResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Payment not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<PaymentResponse>> getPaymentById(
            @PathVariable Integer id,
            HttpServletRequest httpRequest) {

        PaymentResponse response = paymentService.getById(id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Payment retrieved successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    // =========================
    // UPDATE PAYMENT
    // =========================
    @PutMapping("/{id}")
    @Operation(
            summary = "Update payment",
            description = "Update payment information by payment id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payment updated successfully",
                            content = @Content(schema = @Schema(implementation = PaymentResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Payment not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<PaymentResponse>> updatePayment(
            @PathVariable Integer id,
            @RequestBody @Valid PaymentUpdateRequest request,
            HttpServletRequest httpRequest) {

        PaymentResponse response = paymentService.update(request, id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Payment updated successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }
}