package com.MPS.momer_payments_platform.repository;

import com.MPS.momer_payments_platform.domain.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.accountId = :id")
    Optional<Account> findByIdWithLock(@Param("id") UUID id);
    Optional<Account> findByAccountNumberAndSortCode(String accountNumber,String sortCode);
    boolean existsByAccountNumberAndSortCode(String accountNumber, String sortCode);




}
