package com.be.ssm.entities.sales;

import com.be.ssm.entities.identity.Employees;
import com.be.ssm.entities.store.StoreTables;
import com.be.ssm.enums.sales.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Orders {

    private static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "order_number", nullable = false, unique = true, length = 30)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customers customers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employees employees;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_table_id", nullable = false)
    private StoreTables storeTables;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OrderStatus status;

    @Column(name = "subtotal", nullable = false, precision = 15, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "discount_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "vat", precision = 5, scale = 2)
    private BigDecimal vat; // %

    @Column(name = "tax_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "grand_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal grandTotal;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<OrderItems> orderItems = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    // ===============================
    // BUSINESS LOGIC
    // ===============================

    @PrePersist
    @PreUpdate
    private void prePersist() {
        if (status == null) status = OrderStatus.IN_PROGRESS;
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (orderNumber == null) orderNumber = buildOrderNumber();

        ensureDefaults();
        calculateTotals();
    }

    private void calculateTotals() {
        subtotal = orderItems.stream()
                .map(OrderItems::getLineTotal)
                .filter(Objects::nonNull)
                .reduce(ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        // Tax tính trên subtotal (trước discount)
        taxAmount = subtotal
                .multiply(vat)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        // grandTotal = subtotal - discount + tax
        grandTotal = subtotal
                .subtract(discountAmount)
                .add(taxAmount)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private String buildOrderNumber() {
        String date = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String random = String.valueOf(
                (int) (Math.random() * 900000) + 100000
        );

        return "ORD-" + date + "-" + random;
    }

    public void addItem(OrderItems item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    public void recalculate(){
        ensureDefaults();
        calculateTotals();
    }

    private void ensureDefaults() {
        if (vat == null)            vat            = ZERO;
        if (discountAmount == null) discountAmount = ZERO;
        if (taxAmount == null)      taxAmount      = ZERO;
    }

    @Transient
    public void addItems(List<OrderItems> items) {
        for (OrderItems item : items) {
            addItem(item);
        }
    }

    public void removeItem(OrderItems item) {
        orderItems.remove(item);
        item.setOrder(null);
    }
}