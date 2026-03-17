package com.be.ssm.service.impl.saleImpl;

import com.be.ssm.dto.request.sale.InvoiceCreateRequest;
import com.be.ssm.dto.request.sale.InvoiceUpdateRequest;
import com.be.ssm.dto.response.sale.InvoiceResponse;
import com.be.ssm.entities.sales.Invoices;
import com.be.ssm.entities.sales.Orders;
import com.be.ssm.enums.sales.InvoiceStatus;
import com.be.ssm.enums.sales.OrderStatus;
import com.be.ssm.exceptions.CustomException;
import com.be.ssm.exceptions.Error;
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

        return mapper.toInvoiceResponse(findById(id));
    }

    @Override
    public InvoiceResponse create(InvoiceCreateRequest request) {
        log.info("Create new invoice");
        Orders order = findOrderById(request.getOrderId());

        if(order.getStatus() == OrderStatus.COMPLETED){
            throw new CustomException(Error.ORDER_ALREADY_COMPLETED);
        }

        Invoices invoice = mapper.toInvoiceEntity(request);
        invoice.setOrder(order);

        invoice.setStatus(InvoiceStatus.UNPAID);
        invoice.setTotalAmount(order.getGrandTotal());

        return mapper.toInvoiceResponse(repository.save(invoice));
    }

    @Override
    public InvoiceResponse update(InvoiceUpdateRequest request, Integer id) {
        log.info("Update invoice with id {}", id);
        Orders order = findOrderById(request.getOrderId());

        Invoices invoice = findById(id);
        invoice.setOrder(order);

        mapper.updateEntityFromRequest(request, invoice);

        return mapper.toInvoiceResponse(repository.save(invoice));
    }

    private Invoices findById(Integer id) {
        log.info("Finding invoice by id {}", id);

        return repository.findById(id)
                .orElseThrow(()-> new CustomException(Error.INVOICE_NOT_FOUND));
    }

    private Orders findOrderById(Integer id) {
        log.info("Finding orders by id {}", id);

        return ordersRepository.findById(id)
                .orElseThrow(()-> new CustomException(Error.ORDER_NOT_FOUND));
    }
}