package com.techie.designPattern.StructuralDesign;

/**
 * USE CASE: Cross-Platform Notification System
 * PROBLEM: Decouple abstraction from implementation
 * JDK 17 FEATURE: Sealed interfaces for controlled hierarchies
 */

// Implementation interface
sealed interface MessageSender permits EmailSender, SMSSender, PushSender {
    void send(String message, String recipient);
}

final class EmailSender implements MessageSender {
    @Override
    public void send(String message, String recipient) {
        System.out.println("Email to " + recipient + ": " + message);
    }
}

final class SMSSender implements MessageSender {
    @Override
    public void send(String message, String recipient) {
        System.out.println("SMS to " + recipient + ": " + message);
    }
}

final class PushSender implements MessageSender {
    @Override
    public void send(String message, String recipient) {
        System.out.println("Push notification to " + recipient + ": " + message);
    }
}

// Abstraction
sealed interface Notification permits UrgentNotification, RegularNotification {
    void notify(String message, String recipient);
}

record UrgentNotification(MessageSender sender) implements Notification {
    @Override
    public void notify(String message, String recipient) {
        System.out.println("[URGENT]");
        sender.send("⚠️ " + message, recipient);
    }
}

record RegularNotification(MessageSender sender) implements Notification {
    @Override
    public void notify(String message, String recipient) {
        System.out.println("[INFO]");
        sender.send("ℹ️ " + message, recipient);
    }
}

public class BridgePattern {
    public static void main(String[] args) {
        System.out.println("=== BridgePattern Demo ===");

        MessageSender emailSender = new EmailSender();
        MessageSender smsSender = new SMSSender();
        MessageSender pushSender = new PushSender();

        Notification urgentEmail = new UrgentNotification(emailSender);
        Notification regularSms = new RegularNotification(smsSender);
        Notification urgentPush = new UrgentNotification(pushSender);

        urgentEmail.notify("Server is down!", "ops@example.com");
        regularSms.notify("Daily report is ready.", "+1234567890");
        urgentPush.notify("You have a critical alert.", "user-device-token");

        System.out.println("BridgePattern demo finished.");
    }
}
