package com.be.ssm.entities.identity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Permissions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perm_id")
    private Integer permId;

    @Column(name = "perm_name", nullable = false, length = 100)
    private String permName;

    @Column(name = "perm_code", nullable = false, unique = true, length = 100)
    private String permCode;

    @Column(name = "module", nullable = false, length = 50)
    private String module;

    @Column(name = "description", length = 200)
    private String description;
}
