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
public class RolePermissions {
    @Id
    @Column(name = "role_id")
    private Integer roleId;

    @Id
    @Column(name = "perm_id")
    private Integer permId;
}
