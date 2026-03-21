package com.MPS.momer_payments_platform.util;

import java.util.UUID;

public class PaymentReferenceGenerator {

    public static String generatePaymentReference() {
        return "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}
