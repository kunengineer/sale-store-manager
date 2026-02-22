package com.be.ssm.mapper.sales;

import com.be.ssm.dto.request.sale.PaymentCreateRequest;
import com.be.ssm.dto.request.sale.PaymentUpdateRequest;
import com.be.ssm.dto.response.sale.PaymentResponse;
import com.be.ssm.entities.sales.Payments;
import com.be.ssm.entities.store.StoreTables;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentResponse toResponse(Payments payments);

    Payments fromCreateToEntity(PaymentCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy
            = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(PaymentUpdateRequest request,
                      @MappingTarget Payments entity);
}
