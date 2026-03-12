package com.be.ssm.controller.sale;

import com.be.ssm.dto.common.APIResponse;
import com.be.ssm.dto.request.sale.OrderItemCreateRequest;
import com.be.ssm.dto.request.sale.OrderItemUpdateRequest;
import com.be.ssm.dto.response.sale.OrderItemResponse;
import com.be.ssm.service.sale.OrderItemService;
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
@RequestMapping("/order-items")
@Tag(name = "Order Item Management", description = "APIs for managing order items in system")
public class OrderItemController {

    private final OrderItemService orderItemService;

    // =========================
    // GET ORDER ITEM BY ID
    // =========================
    @GetMapping("/{id}")
    @Operation(
            summary = "Get order item by id",
            description = "Retrieve order item by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order item retrieved successfully",
                            content = @Content(schema = @Schema(implementation = OrderItemResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Order item not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<OrderItemResponse>> getOrderItemById(
            @PathVariable Integer id,
            HttpServletRequest httpRequest) {

        OrderItemResponse response = orderItemService.getById(id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Order item retrieved successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    // =========================
    // UPDATE ORDER ITEM
    // =========================
    @PutMapping("/{id}")
    @Operation(
            summary = "Update order item",
            description = "Update order item information by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order item updated successfully",
                            content = @Content(schema = @Schema(implementation = OrderItemResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Order item not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<OrderItemResponse>> updateOrderItem(
            @PathVariable Integer id,
            @RequestBody @Valid OrderItemUpdateRequest request,
            HttpServletRequest httpRequest) {

        OrderItemResponse response = orderItemService.update(request, id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Order item updated successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    @DeleteMapping
    @Operation(
            summary = "Delete order items",
            description = "Delete multiple order items by list of ids",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order items deleted successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Order item not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<Void>> deleteOrderItems(
            @RequestBody List<Integer> orderItemIds,
            HttpServletRequest httpRequest) {

        orderItemService.delete(orderItemIds);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Order items deleted successfully",
                        null,
                        null,
                        httpRequest.getRequestURI()
                )
        );
    }
}