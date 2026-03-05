package com.be.ssm.entities.sales;

import com.be.ssm.entities.store.Stores;
import com.be.ssm.enums.sales.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "customer_code", nullable = false, unique = true, length = 30)
    private String customerCode;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "phone", unique = true, length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10)
    private Gender gender;

    @Column(name = "loyalty_points", nullable = false)
    private Integer loyaltyPoints;

    @Column(name = "total_spent", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalSpent;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Stores store;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.customerCode = buildCustomerCode();
        if (this.loyaltyPoints == null) this.loyaltyPoints = 0;
        if (this.totalSpent == null) this.totalSpent = BigDecimal.ZERO;
    }

    private String buildCustomerCode() {
        String date = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String random = String.valueOf(
                (int) (Math.random() * 900000) + 100000
        );
        return "CUS-" + date + "-" + random;
    }

}
