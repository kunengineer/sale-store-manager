package com.be.ssm.service.sale;

import com.be.ssm.dto.request.sale.InvoiceCreateRequest;
import com.be.ssm.dto.request.sale.InvoiceUpdateRequest;
import com.be.ssm.dto.request.sale.PayOrderRequest;
import com.be.ssm.dto.response.sale.InvoiceResponse;
import org.springframework.stereotype.Service;

@Service
public interface InvoicesService {
    InvoiceResponse getById(Integer id);

    InvoiceResponse create(InvoiceCreateRequest request);

    InvoiceResponse update(InvoiceUpdateRequest request, Integer invoiceId);
}
