package com.techie.designPattern.creationalDesignPattern;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SingletonTest {

    @Test
    void databaseConnectionPoolIsSingleton() {
        var a = DatabaseConnectionPool.INSTANCE;
        var b = DatabaseConnectionPool.INSTANCE;
        assertSame(a, b, "DatabaseConnectionPool.INSTANCE should always return the same reference");
    }

    @Test
    void configurationManagerIsSingletonAndUpdateStoresSameReference() {
        var original = ConfigurationManager.INSTANCE.getConfig();
        var replacement = new ConfigurationManager.Config("api-key-xyz", original.dbUrl(), original.timeout());

        // replacement is a different instance than original
        assertNotSame(original, replacement);

        // Update the manager with the replacement reference
        ConfigurationManager.INSTANCE.updateConfig(replacement);

        // The manager should now return the same reference we stored
        var readBack = ConfigurationManager.INSTANCE.getConfig();
        assertSame(replacement, readBack, "After updateConfig the stored reference should be identical to the one provided");
    }
}

