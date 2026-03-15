package com.MPS.momer_payments_platform.Repo;

import com.MPS.momer_payments_platform.Domain.Payees;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PayeeRepository extends JpaRepository<Payees, UUID> {
    Optional<Payees> findByOwnerAccountIdAndReceiverAccountId(UUID ownerAccountId, UUID receiverAccountId);
    List<Payees> findAllByOwnerAccountId(UUID ownerAccountId);

}
