package com.be.ssm.controller.account;

import com.be.ssm.dto.common.APIResponse;
import com.be.ssm.dto.request.account.AccountCreateRequest;
import com.be.ssm.dto.request.account.AccountUpdateRequest;
import com.be.ssm.dto.request.account.FormLogin;
import com.be.ssm.dto.response.account.AccountResponse;
import com.be.ssm.dto.response.account.AuthenticationResponse;
import com.be.ssm.service.account.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
@Tag(name = "Account Management", description = "APIs for managing system accounts")
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @Operation(
            summary = "Create new account",
            description = "Create a new account with the provided details",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(  // ← thêm vào đây
                    description = "Account creation payload",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",           // ← bắt buộc phải có mediaType
                            schema = @Schema(implementation = AccountCreateRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Account created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccountResponse.class)
                            )),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<AccountResponse>> create(
            @RequestBody @Valid AccountCreateRequest request,
            HttpServletRequest httpRequest) {
        log.info("Received request to create account with details {}", request);

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
    @Operation(
            summary = "Get account by id",
            description = "Retrieve account information by account id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Account retrieved successfully",
                            content = @Content(schema = @Schema(implementation = AccountResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Account not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
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
    @Operation(
            summary = "Update account",
            description = "Update existing account information by account id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Account updated successfully",
                            content = @Content(schema = @Schema(implementation = AccountResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "Account not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
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

    @PostMapping("/login")
    @Operation(
            summary = "User sign in",
            description = "Authenticate user and return access token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful",
                            content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid username or password"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<APIResponse<AuthenticationResponse>> signIn(
            @RequestBody @Valid FormLogin formLogin,
            HttpServletRequest httpRequest) {

        AuthenticationResponse response = accountService.signIn(formLogin);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Login successful",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }
}