package com.be.ssm.config;

import com.be.ssm.entities.account.Accounts;
import com.be.ssm.entities.identity.Employees;
import com.be.ssm.entities.identity.Roles;
import com.be.ssm.entities.identity.WorkShifts;
import com.be.ssm.entities.store.Stores;
import com.be.ssm.enums.account.AccountRole;
import com.be.ssm.enums.identity.SalaryType;
import com.be.ssm.repository.account.AccountRepository;
import com.be.ssm.repository.identity.EmployeesRepository;
import com.be.ssm.repository.identity.RolesRepository;
import com.be.ssm.repository.identity.WorkShiftsRepository;
import com.be.ssm.repository.store.StoresRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

// config/DataInitializer.java
@Component
@RequiredArgsConstructor
@Slf4j
@Profile("dev")   // ✅ chỉ chạy khi profile = dev, không ảnh hưởng production
public class DataInitializer implements CommandLineRunner {

    private final RolesRepository rolesRepository;
    private final StoresRepository storesRepository;
    private final AccountRepository accountsRepository;
    private final EmployeesRepository employeesRepository;
    private final WorkShiftsRepository workShiftsRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("=== Initializing mock data ===");

        initRoles();
        initStores();
        initWorkShifts();
        initAccounts();
        initEmployees();

        log.info("=== Mock data initialized ===");
    }

    // ── Roles ─────────────────────────────────────────────────
    private void initRoles() {
        if (rolesRepository.count() > 0) {
            log.info("Roles already exists — skip");
            return;   // ✅ không insert 2 lần khi restart
        }
        rolesRepository.saveAll(List.of(
                Roles.builder().roleName("OWNER").build(),
                Roles.builder().roleName("MANAGER").build(),
                Roles.builder().roleName("CASHIER").build(),
                Roles.builder().roleName("WAREHOUSE").build()
        ));
        log.info("Roles initialized");
    }

    // ── Stores ────────────────────────────────────────────────
    private void initStores() {
        if (storesRepository.count() > 0) return;

        storesRepository.saveAll(List.of(
                Stores.builder()
                        .storeName("Chi nhánh Quận 1")
                        .storeCode("STR-001")
                        .address("123 Nguyễn Huệ, Q1, HCM")
                        .isActive(true)
                        .build(),
                Stores.builder()
                        .storeName("Chi nhánh Quận 3")
                        .storeCode("STR-002")
                        .address("456 Võ Văn Tần, Q3, HCM")
                        .isActive(true)
                        .build()
        ));
        log.info("Stores initialized");
    }

    // ── WorkShifts ────────────────────────────────────────────
    private void initWorkShifts() {
        if (workShiftsRepository.count() > 0) return;

        workShiftsRepository.saveAll(List.of(
                WorkShifts.builder()
                        .startTime(LocalDateTime.now())
                        .endTime(LocalDateTime.now())
                        .build(),
                WorkShifts.builder()
                        .startTime(LocalDateTime.now())
                        .endTime(LocalDateTime.now())
                        .build()
        ));
        log.info("WorkShifts initialized");
    }

    // ── Accounts ──────────────────────────────────────────────
    private void initAccounts() {
        if (accountsRepository.count() > 0) return;

        accountsRepository.saveAll(List.of(
                Accounts.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("123456"))  // ✅ tự encode
                        .role(AccountRole.ADMIN)
                        .isActive(true)
                        .build(),
                Accounts.builder()
                        .username("manager1")
                        .password(passwordEncoder.encode("123456"))
                        .role(AccountRole.MANAGER)
                        .isActive(true)
                        .build(),
                Accounts.builder()
                        .username("cashier1")
                        .password(passwordEncoder.encode("123456"))
                        .role(AccountRole.EMPLOYEE)
                        .isActive(true)
                        .build()
        ));
        log.info("Accounts initialized");
    }

    // ── Employees ─────────────────────────────────────────────
    private void initEmployees() {
        if (employeesRepository.count() > 0) return;

        Roles ownerRole = rolesRepository.findByRoleName("OWNER")
                .orElseThrow(() -> new RuntimeException("Role OWNER not found"));

        Roles managerRole = rolesRepository.findByRoleName("MANAGER")
                .orElseThrow(() -> new RuntimeException("Role MANAGER not found"));

        Stores    store1     = storesRepository.findByStoreCode("STR-001")
                .orElseThrow(() -> new RuntimeException("Store STR-001 not found"));
        Accounts  adminAcc   = accountsRepository.findByUsername("admin").orElseThrow();
        Accounts  managerAcc = accountsRepository.findByUsername("manager1").orElseThrow();

        employeesRepository.saveAll(List.of(
                Employees.builder()
                        .empCode("EMP-001")
                        .fullName("Nguyễn Văn An")
                        .phone("0901234567")
                        .email("an.nguyen@salestore.com")
                        .role(ownerRole)
                        .store(store1)
                        .account(adminAcc)
                        .salaryType(SalaryType.MONTHLY)
                        .baseSalary(BigDecimal.valueOf(15_000_000))
                        .hireDate(LocalDateTime.now().minusYears(2))
                        .isActive(true)
                        .build(),
                Employees.builder()
                        .empCode("EMP-002")
                        .fullName("Trần Thị Bình")
                        .phone("0912345678")
                        .email("binh.tran@salestore.com")
                        .role(managerRole)
                        .store(store1)
                        .account(managerAcc)
                        .salaryType(SalaryType.MONTHLY)
                        .baseSalary(BigDecimal.valueOf(12_000_000))
                        .hireDate(LocalDateTime.now().minusYears(1))
                        .isActive(true)
                        .build()
        ));
        log.info("Employees initialized");
    }
}