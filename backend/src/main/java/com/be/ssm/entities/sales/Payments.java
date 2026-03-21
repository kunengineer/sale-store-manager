package com.be.ssm.entities.sales;

import com.be.ssm.enums.sales.PaymentMethod;
import com.be.ssm.enums.sales.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoices invoice;

    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false, length = 20)
    private PaymentMethod method;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "transaction_id", length = 100)
    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PaymentStatus status;

    @Column(name = "amount_tendered", precision = 15, scale = 2)
    private BigDecimal amountTendered;

    @Column(name = "change_amount", precision = 15, scale = 2)
    private BigDecimal changeAmount;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = PaymentStatus.PENDING;
        }
    }
}
