package com.be.ssm.entities.sales;

import com.be.ssm.enums.sales.InvoiceStatus;
import com.be.ssm.enums.sales.InvoiceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Invoices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Integer invoiceId;

    @Column(name = "invoice_number", nullable = false, unique = true, length = 30)
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_type", nullable = false, length = 20)
    private InvoiceType invoiceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private InvoiceStatus status;

    @Column(name = "buyer_name", length = 150)
    private String buyerName;

    @Column(name = "subtotal", nullable = false, precision = 15, scale = 2)
    private BigDecimal subtotal;         // = sum(orderItems.lineTotal)

    @Column(name = "discount_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "vat", precision = 5, scale = 2)
    private BigDecimal vat;              // %

    @Column(name = "tax_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal taxAmount;        // = subtotal * vat / 100

    @Column(name = "grand_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal grandTotal;       // = subtotal - discount + tax

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @Column(name = "pdf_url", length = 255)
    private String pdfUrl;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @PrePersist
    public void prePersist() {
        if (this.invoiceNumber == null) this.invoiceNumber = buildInvoiceNumber();
        if (this.discountAmount == null) this.discountAmount = BigDecimal.ZERO;
        if (this.vat == null)            this.vat = BigDecimal.ZERO;
        this.issuedAt = LocalDateTime.now();
        calculate();
    }

    private void calculate() {
        this.taxAmount = subtotal
                .multiply(vat)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        this.grandTotal = subtotal
                .subtract(discountAmount)
                .add(taxAmount)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private String buildInvoiceNumber() {
        String date = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String random = String.valueOf(
                (int) (Math.random() * 900000) + 100000
        );

        return "INV-" + date + "-" + random;
    }
}
