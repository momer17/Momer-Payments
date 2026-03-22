package com.MPS.momer_payments_platform.api.Controller;

import com.MPS.momer_payments_platform.service.PayeeService;
import com.MPS.momer_payments_platform.api.dto.Payees.CreatePayeeRequest;
import com.MPS.momer_payments_platform.api.dto.Payees.PayeeResponse;
import com.MPS.momer_payments_platform.api.dto.Payees.UpdatePayeeRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payee")
public class PayeeController {
    private final PayeeService payeeService;

    public PayeeController(PayeeService payeeService) {this.payeeService = payeeService;
    }

    @PostMapping //Currently will use polling for updates
    public ResponseEntity<PayeeResponse> createPayeeVerificationPending(@Valid @RequestBody CreatePayeeRequest payeeRequest){
        return ResponseEntity.ok(payeeService.createPayeeVerificationPending(payeeRequest));
    }

    @PutMapping("/{accountId}/name")
    public ResponseEntity<PayeeResponse> updatePayeeDisplayName(@PathVariable UUID accountId, @Valid @RequestBody UpdatePayeeRequest updatePayeeRequest){
        return ResponseEntity.ok(payeeService.updatePayeeDisplayName(updatePayeeRequest));
    }

    @DeleteMapping("/{ownerAccountId}/{receiverAccountId}")
    public ResponseEntity<Void> deletePayee(@PathVariable UUID ownerAccountId, @PathVariable UUID receiverAccountId){
        payeeService.deletePayee(ownerAccountId,receiverAccountId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<List<PayeeResponse>>getPayeesByAccount (@PathVariable UUID accountId){
        return ResponseEntity.ok(payeeService.getAllPayeesByOwnerAccountId(accountId));
    }





}
