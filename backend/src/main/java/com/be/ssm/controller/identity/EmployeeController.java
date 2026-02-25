package com.be.ssm.controller.identity;


import com.be.ssm.dto.common.APIResponse;
import com.be.ssm.dto.request.identity.EmployeeCreateRequest;
import com.be.ssm.dto.request.identity.EmployeeUpdateRequest;
import com.be.ssm.dto.response.identity.EmployeeResponse;
import com.be.ssm.service.identity.EmployeeService;
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
@RequestMapping("/employees")
@Tag(name = "Employee Management", description = "APIs for managing employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping
    @Operation(
            summary = "Create new employee",
            description = "Create a new employee with basic information",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Employee created successfully",
                            content = @Content(schema = @Schema(implementation = EmployeeResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<EmployeeResponse>> create(
            @RequestBody @Valid EmployeeCreateRequest request,
            HttpServletRequest httpRequest) {

        EmployeeResponse response = employeeService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(
                        true,
                        "Employee created successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get employee by id",
            description = "Retrieve employee information by employee id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Employee retrieved successfully",
                            content = @Content(schema = @Schema(implementation = EmployeeResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Employee not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<EmployeeResponse>> getById(
            @PathVariable Integer id,
            HttpServletRequest httpRequest) {

        EmployeeResponse response = employeeService.getById(id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Employee retrieved successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                )
        );
    }

    @GetMapping("/me")
    @Operation(
            summary = "Get current employee",
            description = "Retrieve information of the currently authenticated employee",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Employee retrieved successfully",
                            content = @Content(schema = @Schema(implementation = EmployeeResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Employee not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<EmployeeResponse>> getCurrentEmployee(
            HttpServletRequest httpRequest) {

        EmployeeResponse response = employeeService.getCurrentEmployee();

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Current employee retrieved successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                )
        );
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update employee",
            description = "Update employee information by employee id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Employee updated successfully",
                            content = @Content(schema = @Schema(implementation = EmployeeResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Employee not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<EmployeeResponse>> update(
            @PathVariable Integer id,
            @RequestBody @Valid EmployeeUpdateRequest request,
            HttpServletRequest httpRequest) {

        EmployeeResponse response = employeeService.update(request, id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Employee updated successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                )
        );
    }
}
