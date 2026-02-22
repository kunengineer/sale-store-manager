package com.be.ssm.service.impl.saleImpl;

import com.be.ssm.dto.request.sale.PaymentCreateRequest;
import com.be.ssm.dto.request.sale.PaymentUpdateRequest;
import com.be.ssm.dto.response.sale.PaymentResponse;
import com.be.ssm.entities.sales.Invoices;
import com.be.ssm.entities.sales.Orders;
import com.be.ssm.entities.sales.Payments;
import com.be.ssm.mapper.sales.PaymentMapper;
import com.be.ssm.repository.sales.InvoicesRepository;
import com.be.ssm.repository.sales.PaymentsRepository;
import com.be.ssm.service.sale.PaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentsRepository repository;
    private final InvoicesRepository invoicesRepository;
    private final PaymentMapper mapper;

    @Override
    public PaymentResponse getById(Integer id) {
        log.info("Getting payment by id {}", id);

        return mapper.toResponse(findById(id));
    }

    @Override
    public PaymentResponse create(PaymentCreateRequest request) {
        log.info("Create new payment");
        Invoices invoice = findInvoiceById(request.getInvoiceId());

        Payments payment = mapper.fromCreateToEntity(request);
        payment.setInvoice(invoice);

        return mapper.toResponse(repository.save(payment));
    }

    @Override
    public PaymentResponse update(PaymentUpdateRequest request, Integer id) {
        log.info("Update payment with id {}", id);
        Invoices invoice = findInvoiceById(request.getInvoiceId());

        Payments payment = findById(id);
        payment.setInvoice(invoice);

        mapper.updateEntity(request, payment);

        return mapper.toResponse(repository.save(payment));
    }

    private Payments findById(Integer id) {
        log.info("Finding payment by id {}", id);

        return repository.findById(id)
                .orElseThrow();
    }

    private Invoices findInvoiceById(Integer id) {
        log.info("Finding order by id {}", id);

        return invoicesRepository.findById(id)
                .orElseThrow();
    }
}
