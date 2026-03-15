package com.MPS.momer_payments_platform.Repo;

import com.MPS.momer_payments_platform.Domain.Account;
import com.MPS.momer_payments_platform.Domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    boolean existsByIdempotencyKey(UUID idempotencyKey);
    //TODO: Implement payemnt lookup by idempotency key
    Optional<Transaction> findByIdempotencyKey(UUID idempotencyKey);
    //TODO: Implement method to get all trnasaction per sender
    List<Transaction> findAllBySender(Account sender);


}
