package com.be.ssm.repository.identity;

import com.be.ssm.entities.identity.WorkShifts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkShiftsRepository extends JpaRepository<WorkShifts, Integer> {
    //Optional<WorkShifts> findByShift(String shiftName);
}
