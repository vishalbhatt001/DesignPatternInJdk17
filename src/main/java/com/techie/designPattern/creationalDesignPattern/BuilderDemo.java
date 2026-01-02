package com.techie.designPattern.creationalDesignPattern;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * USE CASE: Building complex HTTP Request objects
 * PROBLEM: Too many constructor parameters, optional fields
 * JDK 17 FEATURE: Records with builder pattern
 */

// Immutable HTTP Request using record
record HttpRequest(
        String url,
        String method,
        Map<String, String> headers,
        String body,
        int timeout,
        boolean followRedirects
) {
    // Compact constructor for validation
    public HttpRequest {
        Objects.requireNonNull(url, "URL cannot be null");
        Objects.requireNonNull(method, "Method cannot be null");
        if (timeout < 0) {
            throw new IllegalArgumentException("Timeout must be positive");
        }
    }

    // Builder class
    public static class Builder {
        private String url;
        private String method = "GET";
        private Map<String, String> headers = new HashMap<>();
        private String body = "";
        private int timeout = 30;
        private boolean followRedirects = true;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder header(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder followRedirects(boolean follow) {
            this.followRedirects = follow;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(url, method, new HashMap<>(headers),
                    body, timeout, followRedirects);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}

// Usage
class BuilderDemo {
    public static void main(String[] args) {
        HttpRequest request = HttpRequest.builder()
                .url("https://api.example.com/users")
                .method("POST")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer token123")
                .body("""
                        {
                            "name": "John Doe",
                            "email": "john@example.com"
                        }
                        """)
                .timeout(60)
                .build();

        System.out.println("Request URL: " + request);
    }
}

