package com.be.ssm.dto.request.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
public class StoreUpdateRequest {
    @NotBlank(message = "Store name must not be blank")
    @Size(max = 100, message = "Store name must not exceed 100 characters")
    @Schema(example = "Highland Coffee Nguyen Trai")
    private String storeName;

    @NotBlank(message = "Address must not be blank")
    @Schema(example = "123 Nguyen Trai Street, District 1, Ho Chi Minh City")
    private String address;

    @Pattern(regexp = "^(0|\\+84)[0-9]{9,10}$",
            message = "Phone number is invalid")
    @Schema(example = "0912345678")
    private String phone;

    @Email(message = "Email is invalid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Schema(example = "store@example.com")
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Schema(type = "string", example = "08:00")
    private LocalTime openTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Schema(type = "string", example = "22:00")
    private LocalTime closeTime;

    @Schema(example = "true")
    private Boolean isActive;
}
