package com.be.ssm.mapper.sales;

import com.be.ssm.dto.request.sale.InvoiceCreateRequest;
import com.be.ssm.dto.request.sale.InvoiceUpdateRequest;
import com.be.ssm.dto.response.sale.InvoiceResponse;
import com.be.ssm.entities.sales.Invoices;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface InvoicesMapper {
    InvoiceResponse toInvoiceResponse(Invoices invoices);

    Invoices toInvoiceEntity(InvoiceCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(InvoiceUpdateRequest request,
                                 @MappingTarget Invoices entity);
}
