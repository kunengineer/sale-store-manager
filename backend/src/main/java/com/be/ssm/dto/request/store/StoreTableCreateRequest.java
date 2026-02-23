package com.be.ssm.dto.request.store;

import com.be.ssm.enums.store.TableStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class StoreTableCreateRequest {
    @NotNull(message = "Zone id must not be null")
    @Positive(message = "Zone id must be positive")
    @Schema(example = "1")
    private Integer storeZoneId;

    @NotBlank(message = "Table code must not be blank")
    @Size(max = 20, message = "Table code must not exceed 20 characters")
    @Schema(example = "TBL-01")
    private String tableCode;

    @Positive(message = "Seats must be greater than 0")
    @Schema(example = "4")
    private Integer seats;

    @NotNull(message = "Table status must not be null")
    @Schema(example = "AVAILABLE")
    private TableStatus status;
}
