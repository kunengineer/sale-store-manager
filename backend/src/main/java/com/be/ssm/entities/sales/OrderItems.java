package com.be.ssm.entities.sales;

import com.be.ssm.entities.product.ProductVariants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Integer orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variants_id", nullable = false)
    private ProductVariants productVariants;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "discount_pct", nullable = false, precision = 5, scale = 2)
    private BigDecimal discountPct;

    @Column(name = "discount_amt", nullable = false, precision = 15, scale = 2)
    private BigDecimal discountAmt;

    @Column(name = "line_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal lineTotal;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @PrePersist
    @PreUpdate
    public void prePersist() {

        // ===== DEFAULT VALUE =====
        if (quantity == null) {
            quantity = 1;
        }

        if (unitPrice == null) {
            unitPrice = BigDecimal.ZERO;
        }

        if (discountPct == null) {
            discountPct = BigDecimal.ZERO;
        }

        if (discountAmt == null) {
            discountAmt = BigDecimal.ZERO;
        }

        // ===== TÍNH DISCOUNT AMOUNT (nếu có %) =====
        if (discountPct.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal gross = unitPrice.multiply(BigDecimal.valueOf(quantity));
            discountAmt = gross
                    .multiply(discountPct)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }

        // ===== TÍNH LINE TOTAL =====
        BigDecimal gross = unitPrice.multiply(BigDecimal.valueOf(quantity));

        lineTotal = gross
                .subtract(discountAmt)
                .setScale(2, RoundingMode.HALF_UP);
    }
}