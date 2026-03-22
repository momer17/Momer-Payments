package com.MPS.momer_payments_vop.service;

import com.MPS.momer_payments_vop.domain.VerificationRecord;
import com.MPS.momer_payments_vop.events.VopRequestEvent;
import com.MPS.momer_payments_vop.events.VopResponseEvent;
import com.MPS.momer_payments_vop.matching.NameMatchResult;
import com.MPS.momer_payments_vop.matching.NameMatcher;
import com.MPS.momer_payments_vop.publisher.VopResponsePublisher;
import com.MPS.momer_payments_vop.repository.VopRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class VopService {
    private final NameMatcher nameMatcher;
    private final VopResponsePublisher vopResponsePublisher;
    private final VopRepository vopRepo;

    public VopService(NameMatcher nameMatcher, VopResponsePublisher vopResponsePublisher, VopRepository vopRepo) {
        this.nameMatcher = nameMatcher;
        this.vopResponsePublisher = vopResponsePublisher;
        this.vopRepo = vopRepo;
    }

    public void processMatchRequest(VopRequestEvent vopRequestEvent) {
        NameMatchResult matchResult = nameMatcher.match(vopRequestEvent.requestedName(), vopRequestEvent.actualName());

        VopResponseEvent responseEvent = buildVopResponse(vopRequestEvent, matchResult);
        vopResponsePublisher.publishMatchCompleted(responseEvent);

        buildAndSaveVopRecord(responseEvent, vopRequestEvent, matchResult);
    }

    private VopResponseEvent buildVopResponse(VopRequestEvent vopRequestEvent, NameMatchResult matchResult) {
        return new VopResponseEvent(
                vopRequestEvent.requestedName(),
                vopRequestEvent.actualName(),
                matchResult.matchResult().toString(),
                vopRequestEvent.correlationId(),
                matchResult.confidence(),
                Instant.now()
        );
    }

    private VerificationRecord buildAndSaveVopRecord(VopResponseEvent vopResponseEvent, VopRequestEvent vopRequestEvent, NameMatchResult matchResult) {
        VerificationRecord verificationRecord = VerificationRecord.builder()
                .requestedName(vopResponseEvent.requestedName())
                .actualName(vopResponseEvent.actualName())
                .matchResult(matchResult.matchResult())
                .confidence(matchResult.confidence())
                .verifiedAt(Instant.now())
                .build();

        return vopRepo.save(verificationRecord);
    }
}
