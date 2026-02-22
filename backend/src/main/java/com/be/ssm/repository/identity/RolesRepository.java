package com.be.ssm.repository.identity;

import com.be.ssm.entities.identity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Integer> {
}
