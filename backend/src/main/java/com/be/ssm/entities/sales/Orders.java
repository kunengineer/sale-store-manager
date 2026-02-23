package com.be.ssm.entities.sales;

import com.be.ssm.entities.identity.Employees;
import com.be.ssm.entities.store.StoreTables;
import com.be.ssm.entities.store.Stores;
import com.be.ssm.enums.sales.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Orders {
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
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "subtotal", nullable = false, precision = 15, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "discount_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "vat", precision = 15, scale = 2)
    private BigDecimal vat = BigDecimal.ZERO;

    @Column(name = "tax_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "grand_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal grandTotal = BigDecimal.ZERO;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,   // ← save Orders → tự save OrderItems
            orphanRemoval = true         // ← xóa Orders → tự xóa OrderItems
    )
    private List<OrderItems> orderItems = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.orderNumber == null) {
            this.orderNumber = buildOrderNumber();
        }
    }

    private String buildOrderNumber() {
        String date = LocalDate.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd")
        );
        String random = String.valueOf(
                (int)(Math.random() * 900000) + 100000  // 6 chữ số
        );
        return "ORD-" + date + "-" + random;
        // → ORD-20250623-482910
    }

}
