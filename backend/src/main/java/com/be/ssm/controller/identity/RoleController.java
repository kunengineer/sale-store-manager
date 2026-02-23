package com.be.ssm.controller.identity;

import com.be.ssm.dto.common.APIResponse;
import com.be.ssm.dto.request.identity.RoleCreateRequest;
import com.be.ssm.dto.request.identity.RoleUpdateRequest;
import com.be.ssm.dto.response.identity.RoleResponse;
import com.be.ssm.service.identity.RoleService;
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
@RequestMapping("/roles")
@Tag(name = "Role Management", description = "APIs for managing employee roles")
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    @Operation(
            summary = "Create new role",
            description = "Create a new employee role",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Role created successfully",
                            content = @Content(schema = @Schema(implementation = RoleResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<RoleResponse>> create(
            @RequestBody @Valid RoleCreateRequest request,
            HttpServletRequest httpRequest) {

        RoleResponse response = roleService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(
                        true,
                        "Role created successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get role by id",
            description = "Retrieve role information by role id"
    )
    public ResponseEntity<APIResponse<RoleResponse>> getById(
            @PathVariable Integer id,
            HttpServletRequest httpRequest) {

        RoleResponse response = roleService.getById(id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Role retrieved successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update role",
            description = "Update role information by role id"
    )
    public ResponseEntity<APIResponse<RoleResponse>> update(
            @PathVariable Integer id,
            @RequestBody @Valid RoleUpdateRequest request,
            HttpServletRequest httpRequest) {

        RoleResponse response = roleService.update(request, id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Role updated successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }
}
