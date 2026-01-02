package com.techie.designPattern.StructuralDesign;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class BridgePatternTest {

    private String captureOutput(Runnable action) {
        PrintStream originalOut = System.out;
        var baos = new ByteArrayOutputStream();
        try (var ps = new PrintStream(baos)) {
            System.setOut(ps);
            action.run();
        } finally {
            System.setOut(originalOut);
        }
        return baos.toString();
    }

    @Test
    void emailSender_sendsExpectedMessage() {
        String out = captureOutput(() -> new EmailSender().send("Hello", "user@example.com"));
        assertTrue(out.contains("Email to user@example.com: Hello"), "Email sender should print recipient and message");
    }

    @Test
    void smsSender_sendsExpectedMessage() {
        String out = captureOutput(() -> new SMSSender().send("Hi", "+1000000000"));
        assertTrue(out.contains("SMS to +1000000000: Hi"), "SMS sender should print recipient and message");
    }

    @Test
    void pushSender_sendsExpectedMessage() {
        String out = captureOutput(() -> new PushSender().send("Ping", "device-token"));
        assertTrue(out.contains("Push notification to device-token: Ping"), "Push sender should print recipient and message");
    }

    @Test
    void urgentNotification_addsPrefixAndSends() {
        var urgent = new UrgentNotification(new EmailSender());
        String out = captureOutput(() -> urgent.notify("Server is down", "ops@example.com"));
        assertTrue(out.contains("[URGENT]"), "Should print urgent header");
        assertTrue(out.contains("Email to ops@example.com: ⚠️ Server is down"), "Should send flagged message via EmailSender");
    }

    @Test
    void regularNotification_addsPrefixAndSends() {
        var regular = new RegularNotification(new SMSSender());
        String out = captureOutput(() -> regular.notify("Report ready", "+1111111111"));
        assertTrue(out.contains("[INFO]"), "Should print info header");
        assertTrue(out.contains("SMS to +1111111111: ℹ️ Report ready"), "Should send informational message via SMSSender");
    }

    @Test
    void mainRuns_andProducesDemoOutput() {
        String out = captureOutput(() -> BridgePattern.main(new String[0]));
        assertTrue(out.contains("=== BridgePattern Demo ==="));
        assertTrue(out.contains("BridgePattern demo finished."));
    }
}

