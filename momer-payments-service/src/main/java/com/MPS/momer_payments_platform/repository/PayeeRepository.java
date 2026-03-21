package com.MPS.momer_payments_platform.repository;

import com.MPS.momer_payments_platform.domain.Payees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PayeeRepository extends JpaRepository<Payees, UUID> {
    Optional<Payees> findByOwnerAccountAccountIdAndReceiverAccountAccountId(UUID ownerId, UUID receiverId);
    List<Payees> findAllByOwnerAccountAccountId(UUID ownerAccountId);

}
