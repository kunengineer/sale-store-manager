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
    private Integer storeZoneId;
    private String tableCode;
    private Integer seats;
}
