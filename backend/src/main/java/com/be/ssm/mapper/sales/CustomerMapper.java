package com.be.ssm.mapper.sales;

import com.be.ssm.dto.request.sale.CustomerCreateRequest;
import com.be.ssm.dto.request.sale.CustomerUpdateRequest;
import com.be.ssm.dto.response.sale.CustomerResponse;
import com.be.ssm.dto.response.sale.PosCustomerResponse;
import com.be.ssm.entities.sales.Customers;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerResponse toCustomerResponse(Customers customers);

    Customers toCustomerEntity(CustomerCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(CustomerUpdateRequest request,
                                 @MappingTarget Customers entity);

    PosCustomerResponse toPosCustomerResponse(Customers customers);
}
