package com.be.ssm.dto.request.sale;

import com.be.ssm.enums.sales.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerUpdateRequest {
    @NotBlank(message = "Full name must not be blank")
    @Size(max = 150, message = "Full name must not exceed 150 characters")
    @Schema(example = "Nguyen Van A")
    private String fullName;

    @Pattern(regexp = "^(0|\\+84)[0-9]{9,10}$",
            message = "Phone number is invalid")
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    @Schema(example = "0912345678")
    private String phone;

    @Schema(example = "MALE")
    private Gender gender;

    @Size(max = 1000, message = "Note must not exceed 1000 characters")
    @Schema(example = "VIP customer")
    private String note;
}
