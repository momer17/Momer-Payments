package com.MPS.momer_payments_vop.domain;

import com.MPS.momer_payments_vop.enums.MatchResult;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String requestedName;

    private String actualName;

    @Enumerated(EnumType.STRING)
    private MatchResult matchResult;

    private double confidence;

    private Instant verifiedAt;

}
