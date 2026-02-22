package com.be.ssm.entities.revenue;

import com.be.ssm.entities.store.Stores;
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
public class RevenueSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "snapshot_id")
    private Integer snapshotId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Stores store;

    @Column(name = "snapshot_date", nullable = false)
    private String snapshotDate;

    @Column(name = "gross_revenue", nullable = false, precision = 15, scale = 2)
    private BigDecimal grossRevenue;

    @Column(name = "discount_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal discountTotal;

    @Column(name = "net_revenue", nullable = false, precision = 15, scale = 2)
    private BigDecimal netRevenue;

    @Column(name = "cogs", nullable = false, precision = 15, scale = 2)
    private BigDecimal cogs; // Cost of Goods Sold

    @Column(name = "gross_profit", nullable = false, precision = 15, scale = 2)
    private BigDecimal grossProfit; // net_revenue - cogs

    @Column(name = "tax_collected", nullable = false, precision = 15, scale = 2)
    private BigDecimal taxCollected;

    @Column(name = "cash_sales", nullable = false, precision = 15, scale = 2)
    private BigDecimal cashSales;

    @Column(name = "card_sales", nullable = false, precision = 15, scale = 2)
    private BigDecimal cardSales;

    @Column(name = "online_sales", nullable = false, precision = 15, scale = 2)
    private BigDecimal onlineSales;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
