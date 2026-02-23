package com.be.ssm.controller.account;

import com.be.ssm.dto.common.APIResponse;
import com.be.ssm.dto.request.account.AccountCreateRequest;
import com.be.ssm.dto.request.account.AccountUpdateRequest;
import com.be.ssm.dto.response.account.AccountResponse;
import com.be.ssm.service.account.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
@Tag(name = "Account Management", description = "APIs for managing system accounts")
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<APIResponse<AccountResponse>> create(
            @RequestBody @Valid AccountCreateRequest request,
            HttpServletRequest httpRequest) {

        AccountResponse response = accountService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(
                        true,
                        "Account created successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<AccountResponse>> getById(
            @PathVariable Integer id,
            HttpServletRequest httpRequest) {

        AccountResponse response = accountService.getById(id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Account retrieved successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<AccountResponse>> update(
            @PathVariable Integer id,
            @RequestBody @Valid AccountUpdateRequest request,
            HttpServletRequest httpRequest) {

        AccountResponse response = accountService.update(request, id);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Account updated successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }
}
