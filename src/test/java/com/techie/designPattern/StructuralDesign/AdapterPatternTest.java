package com.techie.designPattern.StructuralDesign;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdapterPatternTest {

    @Test
    void stripeAdapter_processPayment_returnsStripeIdAndSuccess() {
        PaymentGateway gateway = new StripeAdapter();
        var result = gateway.processPayment(12.34, "USD");

        assertTrue(result.success(), "Stripe result should report success");
        assertNotNull(result.transactionId(), "Stripe txn id should not be null");
        assertTrue(result.transactionId().startsWith("stripe_"), "Stripe txn id should start with 'stripe_'");
        assertEquals("Stripe payment successful", result.message());
    }

    @Test
    void paypalAdapter_processPayment_returnsCompletedMessage() {
        PaymentGateway gateway = new PayPalAdapter();
        var result = gateway.processPayment(20.00, "USD");

        assertTrue(result.success(), "PayPal should report success when status is COMPLETED");
        assertNotNull(result.transactionId());
        assertTrue(result.transactionId().startsWith("pp_"), "PayPal txn id should start with 'pp_'");
        assertEquals("PayPal payment COMPLETED", result.message());
    }

    @Test
    void razorpayAdapter_processPayment_returnsRzpIdAndMessage() {
        PaymentGateway gateway = new RazorpayAdapter();
        var result = gateway.processPayment(500.0, "INR");

        assertTrue(result.success(), "Razorpay adapter should report success");
        assertNotNull(result.transactionId());
        assertTrue(result.transactionId().startsWith("rzp_"), "Razorpay id should start with 'rzp_'");
        assertEquals("Razorpay order created", result.message());
    }

    @Test
    void paymentProcessor_withNullGateway_throwsNpe() {
        assertThrows(NullPointerException.class, () -> PaymentProcessor.process(null, 10.0));
    }
}

