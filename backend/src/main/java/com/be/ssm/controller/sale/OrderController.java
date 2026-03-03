package com.be.ssm.controller.sale;

import com.be.ssm.dto.common.APIResponse;
import com.be.ssm.dto.common.PageDTO;
import com.be.ssm.dto.filter.OrderFilter;
import com.be.ssm.dto.request.sale.OrderCreateRequest;
import com.be.ssm.dto.request.sale.OrderItemCreateRequest;
import com.be.ssm.dto.request.sale.OrderUpdateRequest;
import com.be.ssm.dto.response.sale.OrderResponse;
import com.be.ssm.service.sale.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/orders")
@Tag(name = "Order Management", description = "APIs for managing orders in system")
public class OrderController {

    private final OrderService orderService;

    // =========================
    // CREATE ORDER
    // =========================
    @PostMapping
    @Operation(
            summary = "Create new order",
            description = "Create a new order with order details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Order created successfully",
                            content = @Content(schema = @Schema(implementation = OrderResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<OrderResponse>> createOrder(
            @RequestBody @Valid OrderCreateRequest request,
            HttpServletRequest httpRequest) {

        OrderResponse response = orderService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(
                        true,
                        "Order created successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    // =========================
    // GET ORDER BY ID
    // =========================
    @GetMapping("/{id}")
    @Operation(
            summary = "Get order by id",
            description = "Retrieve order information by order id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order retrieved successfully",
                            content = @Content(schema = @Schema(implementation = OrderResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Order not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<OrderResponse>> getOrderById(
            @PathVariable Integer id,
            HttpServletRequest httpRequest) {

        OrderResponse response = orderService.getById(id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Order retrieved successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    @GetMapping("/table/{tableId}")
    @Operation(
            summary = "Get order by table id",
            description = "Retrieve current order information by table id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order retrieved successfully",
                            content = @Content(schema = @Schema(implementation = OrderResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Order not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<OrderResponse>> getOrderByTable(
            @PathVariable Integer tableId,
            HttpServletRequest httpRequest) {

        OrderResponse response = orderService.getByTable(tableId);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Order retrieved successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                )
        );
    }

    // =========================
    // UPDATE ORDER
    // =========================
    @PutMapping("/{id}")
    @Operation(
            summary = "Update order",
            description = "Update order information by order id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order updated successfully",
                            content = @Content(schema = @Schema(implementation = OrderResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Order not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<OrderResponse>> updateOrder(
            @PathVariable Integer id,
            @RequestBody @Valid OrderUpdateRequest request,
            HttpServletRequest httpRequest) {

        OrderResponse response = orderService.update(request, id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Order updated successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    @PostMapping("/{orderId}/items")
    @Operation(
            summary = "Add items to order",
            description = "Add new items into an existing order",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Items added successfully",
                            content = @Content(schema = @Schema(implementation = OrderResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Order not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<OrderResponse>> addItems(
            @Parameter(description = "Order ID", required = true)
            @PathVariable Integer orderId,

            @RequestBody @Valid List<OrderItemCreateRequest> newItems,

            HttpServletRequest httpRequest
    ) {

        OrderResponse response = orderService.addItems(orderId, newItems);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Items added to order successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                )
        );
    }

    @GetMapping
    @Operation(
            summary = "Filter & get orders",
            description = "Retrieve list of orders with filter and pagination"
    )
    public ResponseEntity<APIResponse<PageDTO<OrderResponse>>> getAllOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @ModelAttribute OrderFilter filter,
            HttpServletRequest httpRequest
    ) {

        PageDTO<OrderResponse> response = orderService.getAll(page, size, filter);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Orders retrieved successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                )
        );
    }
}