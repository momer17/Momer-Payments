package com.MPS.momer_payments_platform.api.Controller;

import com.MPS.momer_payments_platform.service.PaymentService;
import com.MPS.momer_payments_platform.api.dto.Payment.CreatePaymentRequest;
import com.MPS.momer_payments_platform.api.dto.Payment.PaymentResponse;
import com.MPS.momer_payments_platform.api.dto.Transaction.TransactionResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/payments")
public class PaymentsController {

    private final PaymentService paymentService;

    public PaymentsController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentResponse createPaymentResponse(@Valid @RequestBody CreatePaymentRequest paymentRequest){
        return paymentService.createPayment(paymentRequest);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable UUID paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentById(paymentId));
    }

    @GetMapping("/transactions/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionHistory(@PathVariable UUID accountId) {
        return ResponseEntity.ok(paymentService.getTransactionHistory(accountId));
    }

    @GetMapping("/transactions/details/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable UUID transactionId) {
        return ResponseEntity.ok(paymentService.getTransactionById(transactionId));
    }

}
