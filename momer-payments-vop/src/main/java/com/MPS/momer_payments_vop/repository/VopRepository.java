package com.MPS.momer_payments_vop.repository;

import com.MPS.momer_payments_vop.domain.VerificationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VopRepository extends JpaRepository<VerificationRecord, UUID>{




}
