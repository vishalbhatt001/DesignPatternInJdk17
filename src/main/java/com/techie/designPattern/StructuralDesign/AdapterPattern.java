package com.techie.designPattern.StructuralDesign;


import java.util.Map;
import java.util.UUID;

/**
 * USE CASE: Payment Gateway Integration
 * PROBLEM: Incompatible interfaces between payment gateways
 * JDK 17 FEATURE: Records + Sealed interfaces
 */

// Target interface
sealed interface PaymentGateway permits StripeAdapter, PayPalAdapter, RazorpayAdapter {
    record PaymentResult(boolean success, String transactionId, String message) {}
    PaymentResult processPayment(double amount, String currency);
}

// Adaptee 1: Stripe API
class StripeAPI {
    public String charge(int amountInCents, String curr) {
        System.out.println("Stripe: Charging " + amountInCents + " cents");
        return "stripe_" + UUID.randomUUID();
    }
}

// Adapter 1
final class StripeAdapter implements PaymentGateway {
    private final StripeAPI stripe = new StripeAPI();

    @Override
    public PaymentResult processPayment(double amount, String currency) {
        int cents = (int) (amount * 100);
        String txnId = stripe.charge(cents, currency);
        return new PaymentResult(true, txnId, "Stripe payment successful");
    }
}

// Adaptee 2: PayPal API
class PayPalAPI {
    public record PayPalResponse(String id, String status) {}

    public PayPalResponse makePayment(String amt, String cur) {
        System.out.println("PayPal: Processing " + amt + " " + cur);
        return new PayPalResponse("pp_" + UUID.randomUUID(), "COMPLETED");
    }
}

// Adapter 2
final class PayPalAdapter implements PaymentGateway {
    private final PayPalAPI paypal = new PayPalAPI();

    @Override
    public PaymentResult processPayment(double amount, String currency) {
        var response = paypal.makePayment(String.valueOf(amount), currency);
        boolean success = "COMPLETED".equals(response.status());
        return new PaymentResult(success, response.id(),
                "PayPal payment " + response.status());
    }
}

// Adaptee 3: Razorpay API
class RazorpayAPI {
    public Map<String, Object> createOrder(Map<String, Object> params) {
        System.out.println("Razorpay: Creating order " + params);
        return Map.of("id", "rzp_" + UUID.randomUUID(),
                "status", "created");
    }
}

// Adapter 3
final class RazorpayAdapter implements PaymentGateway {
    private final RazorpayAPI razorpay = new RazorpayAPI();

    @Override
    public PaymentResult processPayment(double amount, String currency) {
        Map<String, Object> params = Map.of("amount", amount, "currency", currency);
        var order = razorpay.createOrder(params);
        return new PaymentResult(true, (String) order.get("id"),
                "Razorpay order created");
    }
}

// Payment processor with pattern matching
class PaymentProcessor {
    public static void process(PaymentGateway gateway, double amount) {
        var result = switch (gateway) {
            case StripeAdapter s -> s.processPayment(amount, "USD");
            case PayPalAdapter p -> p.processPayment(amount, "USD");
            case RazorpayAdapter r -> r.processPayment(amount, "INR");
        };

        System.out.println("Result: " + result.message());
    }
}


public class AdapterPattern {
    public static void main(String[] args) {
        System.out.println("=== AdapterPattern Demo ===");

        PaymentGateway stripe = new StripeAdapter();
        PaymentGateway paypal = new PayPalAdapter();
        PaymentGateway razorpay = new RazorpayAdapter();

        // Use the processor (pattern matching inside)
        PaymentProcessor.process(stripe, 10.50);
        PaymentProcessor.process(paypal, 20.00);
        PaymentProcessor.process(razorpay, 500.00);

        // Direct call to demonstrate result object and accessors
        var result = stripe.processPayment(15.75, "USD");
        System.out.println("Direct call - success: " + result.success() + ", id: " + result.transactionId() + ", msg: " + result.message());
    }
}
