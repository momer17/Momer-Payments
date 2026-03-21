package com.MPS.momer_payments_platform.repository;

import com.MPS.momer_payments_platform.domain.Account;
import com.MPS.momer_payments_platform.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    boolean existsByIdempotencyKey(String idempotencyKey);
    //TODO: Implement payemnt lookup by idempotency key
    Optional<Transaction> findByIdempotencyKey(UUID idempotencyKey);
    List<Transaction> findAllBySender(Account sender);
    List<Transaction> findAllByReceiver(Account receiver);
    List<Transaction> findAllBySenderOrReceiver(Account sender, Account receiver);


}
