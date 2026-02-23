package com.be.ssm.controller.identity;


import com.be.ssm.dto.common.APIResponse;
import com.be.ssm.dto.request.identity.EmployeeCreateRequest;
import com.be.ssm.dto.request.identity.EmployeeUpdateRequest;
import com.be.ssm.dto.response.identity.EmployeeResponse;
import com.be.ssm.service.identity.EmployeeService;
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
                ));
    }

    @PutMapping("/{id}")
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
                ));
    }
}
