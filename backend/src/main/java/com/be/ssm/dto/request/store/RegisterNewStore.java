package com.be.ssm.dto.request.store;

import com.be.ssm.dto.request.identity.EmployeeCreateRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class RegisterNewStore {

    @NotBlank(message = "Store code must not be blank")
    @Size(max = 20, message = "Store code must not exceed 20 characters")
    @Schema(example = "STR001")
    private String storeCode;

    @NotBlank(message = "Store name must not be blank")
    @Size(max = 100, message = "Store name must not exceed 100 characters")
    @Schema(example = "Highland Coffee Nguyen Trai")
    private String storeName;

    @NotBlank(message = "Address must not be blank")
    @Schema(example = "123 Nguyen Trai Street, District 1, Ho Chi Minh City")
    private String address;

    @Schema(
            description = "Store contact email",
            example = "store@gmail.com"
    )
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Schema(type = "string", example = "08:00")
    private LocalTime openTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Schema(type = "string", example = "22:00")
    private LocalTime closeTime;

    private EmployeeCreateRequest employee;

}