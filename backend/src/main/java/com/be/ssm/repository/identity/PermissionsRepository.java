package com.be.ssm.repository.identity;

import com.be.ssm.entities.identity.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionsRepository extends JpaRepository<Permissions, Integer> {
}
