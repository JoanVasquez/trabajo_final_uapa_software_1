package com.uapa.software;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;

import com.uapa.software.utils.DefaultDotenvProvider;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultDotenvProviderTest {

    @Test
    void testGetDotenvForTestEnvironment() {
        // Arrange
        DefaultDotenvProvider dotenvProvider = new DefaultDotenvProvider();
        String environment = "test";

        // Act
        Dotenv dotenv = dotenvProvider.getDotenv(environment);

        // Assert
        assertNotNull(dotenv, "Dotenv should not be null for the test environment");
        assertEquals(null, dotenv.get("TEST_KEY"), "Expected value for TEST_KEY not found");
    }

    @Test
    void testGetDotenvForProductionEnvironment() {
        // Arrange
        DefaultDotenvProvider dotenvProvider = new DefaultDotenvProvider();
        String environment = "prod"; // Any value other than "test" or "local"

        // Act
        Dotenv dotenv = dotenvProvider.getDotenv(environment);

        // Assert
        assertNotNull(dotenv, "Dotenv should not be null for the production environment");
        assertEquals(null, dotenv.get("PROD_KEY"), "Expected value for PROD_KEY not found");
    }
}
