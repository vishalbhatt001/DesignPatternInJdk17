
Overview
--------
This small project demonstrates two simple, safe singleton examples using modern Java (JDK 17):

1. DatabaseConnectionPool — an enum-based singleton that represents a connection pool manager.
2. ConfigurationManager — an enum-based singleton that holds a `record` configuration object and shows how to read and update it.


Files
-----
- src/main/java/com/techie/designPattern/creationalDesignPattern/SingletonDemo.java
  - Contains both examples and a small `main` method that runs them.

Quick prerequisites
-------------------
- JDK 17 or newer installed and active (records and other features require Java 16+; the examples were written for 17).
- Maven installed for building (optional; you can also run compiled classes directly).

Example 1 — DatabaseConnectionPool (enum singleton)
---------------------------------------------------
Why use an enum for a singleton?
- Java guarantees that each enum value is instantiated exactly once.
- Enums are thread-safe and handle serialization automatically.
- This makes them the simplest and most robust way to write a singleton.

Step-by-step (simple):
1. `enum DatabaseConnectionPool { INSTANCE; }` — The enum defines a single value named `INSTANCE`. That value is the singleton instance.
2. Add fields for the state you need (for example, `private final int maxConnections;`).
3. Add a private constructor (the enum constructor) to initialize state. It's called once when `INSTANCE` is created.
4. Add public methods to operate on the singleton, for example `getMaxConnections()` or `acquireConnection()`.
5. Use the singleton in code with `DatabaseConnectionPool.INSTANCE`.

Tiny code excerpt (see the full file for full code):

```java
// get the singleton instance
var pool = DatabaseConnectionPool.INSTANCE;
System.out.println("Max connections: " + pool.getMaxConnections());
System.out.println("Acquired: " + pool.acquireConnection());
```

What this demonstrates
- How to create a reliable singleton with minimal code.
- How to call methods on the singleton instance.

Example 2 — ConfigurationManager + record (enum holding a record)
-----------------------------------------------------------------
Why use an enum for a manager and a record for the config?
- The enum gives you single-instance semantics for a manager object.
- A `record` is a concise immutable data carrier perfect for configuration objects. Records automatically provide accessors (getters), equals, hashCode, and toString.

Step-by-step (simple):
1. `enum ConfigurationManager { INSTANCE; ... }` — the manager is a singleton.
2. `public record Config(String apiKey, String dbUrl, int timeout) {}` — a compact, immutable holder for configuration.
3. Manager keeps a `private Config config;` field and initializes it in the enum constructor.
4. Expose `getConfig()` to read the configuration and `updateConfig(Config newConfig)` to replace it.
5. Use the manager with `ConfigurationManager.INSTANCE.getConfig()` and `updateConfig(...)`.

Tiny code excerpt:

```java
var config = ConfigurationManager.INSTANCE.getConfig();
System.out.println("API Key (before): " + config.apiKey());

var newConfig = new ConfigurationManager.Config("api-key-456", config.dbUrl(), config.timeout());
ConfigurationManager.INSTANCE.updateConfig(newConfig);

var updated = ConfigurationManager.INSTANCE.getConfig();
System.out.println("API Key (after): " + updated.apiKey());
```

Why `public record Config`?
- If the record is declared private inside its enum, code outside cannot call the generated accessors like `apiKey()`.
- Making it `public` means callers can read its fields using the record accessors.

Running the demo
----------------
From the project root you can build and run with Maven:

```bash
# compile
mvn -DskipTests compile

# package
mvn -DskipTests package

# run the demo (using the exec plugin if available)
mvn exec:java -Dexec.mainClass="com.techie.designPattern.creationalDesignPattern.SingletonDemo"
```

Or run the compiled class directly (after `mvn -DskipTests compile`):

```bash
# run using the classpath built by Maven
java -cp target/classes com.techie.designPattern.creationalDesignPattern.SingletonDemo
```

Expected output (example)
-------------------------
When you run the demo you should see output similar to:

```
Database pool initialized
Max connections: 10
Acquired: conn-1234567890
API Key (before): api-key-123
API Key (after): api-key-456
```

Notes and tips
--------------
- Enum singletons are recommended unless you have a specific reason to use a different technique (for example, lazy initialization that depends on dynamic external resources). If you need laziness, you can still use holder classes or other patterns safely.
- Records are immutable by default. If you need mutable configuration you can replace the record with a regular class — but prefer immutability for safety.
- If you want me to include a separate example of the classic double-checked-locking singleton (the one using `volatile` and `synchronized`) I can add it and explain the pitfalls and how to implement it safely.

If you'd like
-------------
- I can run `mvn -DskipTests compile` here and paste the output.
- I can add a small JUnit test that asserts the singleton property (e.g., both calls to INSTANCE return the same reference) and that `updateConfig` works.
- I can extract each example to its own source file to make them clearer.


