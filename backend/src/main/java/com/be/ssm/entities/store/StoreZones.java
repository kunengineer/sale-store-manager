package com.be.ssm.entities.store;

import com.be.ssm.enums.store.ZoneType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StoreZones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "zone_id")
    private Integer zoneId;

    // FK → stores.store_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Stores store;

    @Column(name = "zone_name", nullable = false, length = 50)
    private String zoneName;

    @Enumerated(EnumType.STRING)
    @Column(name = "zone_type", nullable = false, length = 20)
    private ZoneType zoneType;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "zone", fetch = FetchType.LAZY)
    private List<StoreTables> tables;
}
