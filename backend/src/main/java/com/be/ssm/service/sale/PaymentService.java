package com.be.ssm.service.sale;

import com.be.ssm.dto.request.sale.PaymentCreateRequest;
import com.be.ssm.dto.request.sale.PaymentUpdateRequest;
import com.be.ssm.dto.response.sale.PaymentResponse;
import com.be.ssm.entities.sales.Invoices;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {
    PaymentResponse getById(Integer id);

    PaymentResponse create(PaymentCreateRequest request);

    PaymentResponse update(PaymentUpdateRequest request, Integer paymentId);

    void pay(Invoices invoices, PaymentCreateRequest request);
}
