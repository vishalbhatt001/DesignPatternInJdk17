package com.techie.designPattern.creationalDesignPattern;

/**
 * USE CASE: Payment Processing System
 * PROBLEM: Create different payment processors without specifying exact class
 * JDK 17 FEATURE: Sealed interfaces + Pattern matching
 */

// Payment hierarchy with sealed interface
sealed interface Payment permits CreditCardPayment, UPIPayment, NetBankingPayment {
    boolean process(double amount);
    String getTransactionId();
}

record CreditCardPayment(String cardNumber, String cvv) implements Payment {
    @Override
    public boolean process(double amount) {
        System.out.println("Processing credit card payment: $" + amount);
        return true;
    }

    @Override
    public String getTransactionId() {
        return "CC-" + System.currentTimeMillis();
    }
}

record UPIPayment(String upiId) implements Payment {
    @Override
    public boolean process(double amount) {
        System.out.println("Processing UPI payment: ₹" + amount);
        return true;
    }

    @Override
    public String getTransactionId() {
        return "UPI-" + System.currentTimeMillis();
    }
}

record NetBankingPayment(String accountNumber, String ifsc) implements Payment {
    @Override
    public boolean process(double amount) {
        System.out.println("Processing net banking: ₹" + amount);
        return true;
    }

    @Override
    public String getTransactionId() {
        return "NB-" + System.currentTimeMillis();
    }
}

class PaymentFactory {
    public static Payment createPayment(String type, String... details) {
        return switch (type.toLowerCase()) {
            case "credit_card" -> new CreditCardPayment(details[0], details[1]);
            case "upi" -> new UPIPayment(details[0]);
            case "netbanking" -> new NetBankingPayment(details[0], details[1]);
            default -> throw new IllegalArgumentException("Unknown payment type");
        };
    }

    // Pattern matching for payment processing
    public static String processPayment(Payment payment, double amount) {
        return switch (payment) {
            case CreditCardPayment cc -> {
                cc.process(amount);
                yield "Credit card processed: " + cc.cardNumber();
            }
            case UPIPayment upi -> {
                upi.process(amount);
                yield "UPI processed: " + upi.upiId();
            }
            case NetBankingPayment nb -> {
                nb.process(amount);
                yield "Net banking processed: " + nb.accountNumber();
            }
        };
    }
}

// Add a top-level public class with a main method to demonstrate the factory
public class FactoryPattern {
    public static void main(String[] args) {
        // Create and process a credit card payment
        Payment cc = PaymentFactory.createPayment("credit_card", "4111111111111111", "123");
        System.out.println(PaymentFactory.processPayment(cc, 100.00));

        // Create and process a UPI payment
        Payment upi = PaymentFactory.createPayment("upi", "user@bank");
        System.out.println(PaymentFactory.processPayment(upi, 250.50));

        // Create and process a net banking payment
        Payment nb = PaymentFactory.createPayment("netbanking", "12345678", "IFSC0001");
        System.out.println(PaymentFactory.processPayment(nb, 500.25));

        // Demonstrate handling of unknown type
        try {
            Payment unknown = PaymentFactory.createPayment("cash");
            System.out.println(PaymentFactory.processPayment(unknown, 50.0));
        } catch (IllegalArgumentException e) {
            System.out.println("Expected error for unknown type: " + e.getMessage());
        }
    }
}
