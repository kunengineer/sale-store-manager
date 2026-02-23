package com.be.ssm.helper;

import com.be.ssm.entities.sales.OrderItems;
import com.be.ssm.entities.sales.Orders;
import com.be.ssm.repository.sales.OrderItemsRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class OrderTotalCalculator {

    private final OrderItemsRepository orderItemRepo;

    public OrderTotalCalculator(OrderItemsRepository orderItemRepo) {
        this.orderItemRepo = orderItemRepo;
    }

    /**
     * Tính từ List trong memory — dùng khi create (chưa save).
     */
    public void recalculateFromItems(Orders order) {
        calculate(order, order.getOrderItems());
    }

    /**
     * Tính từ DB — dùng khi thêm/xóa/sửa item đơn lẻ sau khi order đã tồn tại.
     */
    public void recalculate(Orders order) {
        List<OrderItems> items =
                orderItemRepo.findOrderItemsByOrder_OrderId(order.getOrderId());
        calculate(order, items);
    }

    // Logic tính dùng chung
    private void calculate(Orders order, List<OrderItems> items) {
        BigDecimal subtotal = items.stream()
                .map(OrderItems::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal vat = order.getVat() != null
                ? order.getVat()
                : BigDecimal.ZERO;

        BigDecimal taxAmount = subtotal
                .multiply(vat)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal discount = order.getDiscountAmount() != null
                ? order.getDiscountAmount()
                : BigDecimal.ZERO;

        order.setSubtotal(subtotal);
        order.setTaxAmount(taxAmount);
        order.setGrandTotal(subtotal.add(taxAmount).subtract(discount));
    }
}