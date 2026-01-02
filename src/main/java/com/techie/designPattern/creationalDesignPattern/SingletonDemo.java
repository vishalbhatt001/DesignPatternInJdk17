package com.techie.designPattern.creationalDesignPattern;

/*

Overview
--------
This small project demonstrates two simple, safe singleton examples using modern Java (JDK 17):

1. DatabaseConnectionPool — an enum-based singleton that represents a connection pool manager.
2. ConfigurationManager — an enum-based singleton that holds a `record` configuration object and shows how to read and update it.

Files
-----
- src/main/java/com/techie/designPattern/creationalDesignPattern/SingletonDemo.java
  - Contains both examples and a small `main` method that runs them.

Example 1 — DatabaseConnectionPool (enum singleton)
---------------------------------------------------
Why use an enum for a singleton?
- Java guarantees that each enum value is instantiated exactly once.
- Enums are thread-safe and handle serialization automatically.
- This makes them the simplest and most robust way to write a singleton.



Example 2 — ConfigurationManager + record (enum holding a record)
-----------------------------------------------------------------
Why use an enum for a manager and a record for the config?
- The enum gives you single-instance semantics for a manager object.
- A `record` is a concise immutable data carrier perfect for configuration objects. Records automatically provide accessors (getters), equals, hashCode, and toString.



Why `public record Config`?
- If the record is declared private inside its enum, code outside cannot call the generated accessors like `apiKey()`.
- Making it `public` means callers can read its fields using the record accessors.




Notes and tips
--------------
- Enum singletons are recommended unless you have a specific reason to use a different technique (for example, lazy initialization that depends on dynamic external resources). If you need laziness, you can still use holder classes or other patterns safely.
- Records are immutable by default. If you need mutable configuration you can replace the record with a regular class — but prefer immutability for safety.


/**
 * USE CASE: Database Connection Pool Manager
 * PROBLEM: Need exactly one instance managing database connections
 * JDK 17 FEATURE: Enum-based singleton (thread-safe by design)
 */

// Use enum-based singleton which is simple, thread-safe, and serialization-safe
enum DatabaseConnectionPool {
    INSTANCE;

    private final int maxConnections;

    DatabaseConnectionPool() {
        this.maxConnections = 10;
        System.out.println("Database pool initialized");
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    // Simple simulated acquire method for demo purposes
    public String acquireConnection() {
        return "conn-" + System.nanoTime();
    }
}

// BEST PRACTICE: Enum Singleton (JDK 17 Recommended)
enum ConfigurationManager {
    INSTANCE;

    public record Config(String apiKey, String dbUrl, int timeout) {}

    private Config config;

    ConfigurationManager() {
        // Load configuration
        this.config = new Config("api-key-123", "jdbc:postgresql://localhost", 30);
    }

    public Config getConfig() {
        return config;
    }

    public void updateConfig(Config newConfig) {
        this.config = newConfig;
    }
}

// Usage Example
public class SingletonDemo {
    public static void main(String[] args) {
        // Use the enum-based connection pool
        var pool = DatabaseConnectionPool.INSTANCE;
        System.out.println("Max connections: " + pool.getMaxConnections());
        System.out.println("Acquired: " + pool.acquireConnection());

        var config = ConfigurationManager.INSTANCE.getConfig();
        System.out.println("API Key (before): " + config.apiKey());

        // Update config to demonstrate updateConfig usage and silence IDE warnings
        var newConfig = new ConfigurationManager.Config("api-key-456", config.dbUrl(), config.timeout());
        ConfigurationManager.INSTANCE.updateConfig(newConfig);

        var updated = ConfigurationManager.INSTANCE.getConfig();
        System.out.println("API Key (after): " + updated.apiKey());
    }
}
