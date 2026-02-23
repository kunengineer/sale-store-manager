package com.be.ssm.controller.identity;

import com.be.ssm.dto.common.APIResponse;
import com.be.ssm.dto.request.identity.WorkShiftCreateRequest;
import com.be.ssm.dto.request.identity.WorkShiftUpdateRequest;
import com.be.ssm.dto.response.identity.WorkShiftResponse;
import com.be.ssm.service.identity.WorkShiftService;
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
@RequestMapping("/work-shifts")
@Tag(name = "Work Shift Management", description = "APIs for managing employee work shifts")
public class WorkShiftController {
    private final WorkShiftService workShiftService;

    @PostMapping
    @Operation(summary = "Create new work shift")
    public ResponseEntity<APIResponse<WorkShiftResponse>> create(
            @RequestBody @Valid WorkShiftCreateRequest request,
            HttpServletRequest httpRequest) {

        WorkShiftResponse response = workShiftService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(
                        true,
                        "Work shift created successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get work shift by id")
    public ResponseEntity<APIResponse<WorkShiftResponse>> getById(
            @PathVariable Integer id,
            HttpServletRequest httpRequest) {

        WorkShiftResponse response = workShiftService.getById(id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Work shift retrieved successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update work shift")
    public ResponseEntity<APIResponse<WorkShiftResponse>> update(
            @PathVariable Integer id,
            @RequestBody @Valid WorkShiftUpdateRequest request,
            HttpServletRequest httpRequest) {

        WorkShiftResponse response = workShiftService.update(request, id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Work shift updated successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }
}
