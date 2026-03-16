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
    private String storeCode;
    private String storeName;
    private String address;
    private String email;
    private LocalTime openTime;
    private LocalTime closeTime;
    private EmployeeCreateRequest employee;
}