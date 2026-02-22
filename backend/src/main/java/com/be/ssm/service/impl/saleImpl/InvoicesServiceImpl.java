package com.be.ssm.service.impl.saleImpl;

import com.be.ssm.dto.request.sale.InvoiceCreateRequest;
import com.be.ssm.dto.request.sale.InvoiceUpdateRequest;
import com.be.ssm.dto.response.sale.InvoiceResponse;
import com.be.ssm.entities.sales.Invoices;
import com.be.ssm.entities.sales.Orders;
import com.be.ssm.mapper.sales.InvoicesMapper;
import com.be.ssm.repository.sales.InvoicesRepository;
import com.be.ssm.repository.sales.OrdersRepository;
import com.be.ssm.service.sale.InvoicesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class InvoicesServiceImpl implements InvoicesService {

    private final InvoicesRepository repository;
    private final OrdersRepository ordersRepository;
    private final InvoicesMapper mapper;

    @Override
    public InvoiceResponse getById(Integer id) {
        log.info("Getting invoice by id {}", id);

        return mapper.toResponse(findById(id));
    }

    @Override
    public InvoiceResponse create(InvoiceCreateRequest request) {
        log.info("Create new invoice");
        Orders order = findOrderById(request.getOrderId());

        Invoices invoice = mapper.fromCreateToEntity(request);
        invoice.setOrder(order);

        return mapper.toResponse(repository.save(invoice));
    }

    @Override
    public InvoiceResponse update(InvoiceUpdateRequest request, Integer id) {
        log.info("Update invoice with id {}", id);
        Orders order = findOrderById(request.getOrderId());

        Invoices invoice = findById(id);
        invoice.setOrder(order);

        mapper.updateEntityFromRequest(request, invoice);

        return mapper.toResponse(repository.save(invoice));
    }

    private Invoices findById(Integer id) {
        log.info("Finding invoice by id {}", id);

        return repository.findById(id)
                .orElseThrow();
    }

    private Orders findOrderById(Integer id) {
        log.info("Finding orders by id {}", id);

        return ordersRepository.findById(id)
                .orElseThrow();
    }
}