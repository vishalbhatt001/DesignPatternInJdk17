package com.techie.designPattern.creationalDesignPattern;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FactoryPatternTest {

    @Test
    void createAndProcessCreditCardPayment_success() {
        Payment cc = PaymentFactory.createPayment("credit_card", "4111111111111111", "123");
        String result = PaymentFactory.processPayment(cc, 100.00);
        assertEquals("Credit card processed: 4111111111111111", result);
    }

    @Test
    void createAndProcessUPIPayment_success() {
        Payment upi = PaymentFactory.createPayment("upi", "user@bank");
        String result = PaymentFactory.processPayment(upi, 250.50);
        assertEquals("UPI processed: user@bank", result);
    }

    @Test
    void createAndProcessNetBankingPayment_success() {
        Payment nb = PaymentFactory.createPayment("netbanking", "12345678", "IFSC0001");
        String result = PaymentFactory.processPayment(nb, 500.25);
        assertEquals("Net banking processed: 12345678", result);
    }

    @Test
    void createPayment_unknownType_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> PaymentFactory.createPayment("cash"));
    }

    @Test
    void createPayment_insufficientDetails_throwsArrayIndexOutOfBoundsException() {
        // credit_card expects two details (cardNumber, cvv) - providing none should raise exception
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> PaymentFactory.createPayment("credit_card"));
    }
}

