package com.techie.designPattern.creationalDesignPattern;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BuilderDemoTest {

    @Test
    void buildWithAllFields_success() {
        HttpRequest req = HttpRequest.builder()
                .url("https://api.example.com/users")
                .method("POST")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer token123")
                .body("{\"name\":\"John\"}")
                .timeout(60)
                .followRedirects(false)
                .build();

        assertEquals("https://api.example.com/users", req.url());
        assertEquals("POST", req.method());
        assertEquals(60, req.timeout());
        assertFalse(req.followRedirects());
        assertEquals("application/json", req.headers().get("Content-Type"));
        assertEquals("{\"name\":\"John\"}", req.body());
    }

    @Test
    void buildWithDefaults_success() {
        HttpRequest req = HttpRequest.builder()
                .url("https://example.com")
                .build();

        assertEquals("GET", req.method(), "default method should be GET");
        assertEquals(30, req.timeout(), "default timeout should be 30");
        assertTrue(req.followRedirects(), "default followRedirects should be true");
        assertEquals("", req.body(), "default body should be empty string");
        assertNotNull(req.headers(), "headers map should not be null");
        assertTrue(req.headers().isEmpty(), "default headers should be empty");
    }

    @Test
    void build_nullUrl_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> HttpRequest.builder().url(null).build());
    }

    @Test
    void build_negativeTimeout_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> HttpRequest.builder().url("https://x").timeout(-1).build());
    }

    @Test
    void builderCopiesHeaders_betweenBuilds() {
        var builder = HttpRequest.builder().url("https://copy.test");
        builder.header("X-Key", "v1");
        var req1 = builder.build();

        // mutate builder after first build
        builder.header("X-Key", "v2");
        var req2 = builder.build();

        assertEquals("v1", req1.headers().get("X-Key"), "first built request should keep its header values");
        assertEquals("v2", req2.headers().get("X-Key"), "second built request should reflect subsequent builder changes");
    }

    @Test
    void build_methodNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> HttpRequest.builder().url("https://example.com").method(null).build());
    }

    @Test
    void build_timeoutZero_allowed() {
        HttpRequest req = HttpRequest.builder()
                .url("https://zero.test")
                .timeout(0)
                .build();

        assertEquals(0, req.timeout(), "timeout set to zero should be accepted");
    }

    // --- New focused validation-message tests ---
    @Test
    void build_nullUrl_throwsNpe_withMessage() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> HttpRequest.builder().url(null).build());
        assertEquals("URL cannot be null", ex.getMessage());
    }

    @Test
    void build_nullMethod_throwsNpe_withMessage() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> HttpRequest.builder().url("https://example.com").method(null).build());
        assertEquals("Method cannot be null", ex.getMessage());
    }

    @Test
    void build_negativeTimeout_throwsIae_withMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> HttpRequest.builder().url("https://x").timeout(-5).build());
        assertEquals("Timeout must be positive", ex.getMessage());
    }
}
