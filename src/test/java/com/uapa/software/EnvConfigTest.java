package com.uapa.software;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.uapa.software.utils.DefaultDotenvProvider;
import com.uapa.software.utils.EnvConfig;
import com.uapa.software.utils.IDotenvProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;

class EnvConfigTest {

    @AfterEach
    void resetDotenv() {
        EnvConfig.resetDotenv();
        EnvConfig.setDotenvProvider(new DefaultDotenvProvider()); // Restore default provider
    }

    @Test
    void testGetDotenv_initializesWhenNull_withTestEnvironment() {
        Dotenv dotenvMock = mock(Dotenv.class);
        IDotenvProvider mockProvider = mock(IDotenvProvider.class);

        when(mockProvider.getDotenv("test")).thenReturn(dotenvMock);
        EnvConfig.setDotenvProvider(mockProvider);

        when(dotenvMock.get("KEY")).thenReturn("mock_value");

        System.setProperty("env", "test");
        String result = EnvConfig.get("KEY");

        assertEquals("mock_value", result);
        verify(mockProvider, times(1)).getDotenv("test");
    }

    @Test
    void testGetDotenv_initializesWhenNull_withDefaultEnvironment() {
        Dotenv dotenvMock = mock(Dotenv.class);
        IDotenvProvider mockProvider = mock(IDotenvProvider.class);

        when(mockProvider.getDotenv("default")).thenReturn(dotenvMock);
        EnvConfig.setDotenvProvider(mockProvider);

        when(dotenvMock.get("KEY")).thenReturn("mock_value");

        System.setProperty("env", "default");
        String result = EnvConfig.get("KEY");

        assertEquals("mock_value", result);
        verify(mockProvider, times(1)).getDotenv("default");
    }

    @Test
    void testGetOrDefault_returnsDefaultValueForMissingKey() {
        Dotenv dotenvMock = mock(Dotenv.class);
        IDotenvProvider mockProvider = mock(IDotenvProvider.class);

        when(mockProvider.getDotenv("default")).thenReturn(dotenvMock);
        EnvConfig.setDotenvProvider(mockProvider);

        when(dotenvMock.get("UNKNOWN_KEY", "default_value")).thenReturn("default_value");

        System.setProperty("env", "default");
        String result = EnvConfig.getOrDefault("UNKNOWN_KEY", "default_value");

        assertEquals("default_value", result);
    }

    @Test
    void testGet_throwsExceptionForMissingKey() {
        Dotenv dotenvMock = mock(Dotenv.class);
        IDotenvProvider mockProvider = mock(IDotenvProvider.class);

        when(mockProvider.getDotenv("default")).thenReturn(dotenvMock);
        EnvConfig.setDotenvProvider(mockProvider);

        when(dotenvMock.get("MISSING_KEY")).thenReturn(null);

        System.setProperty("env", "default");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> EnvConfig.get("MISSING_KEY"));
        assertEquals("Environment variable 'MISSING_KEY' is not defined", exception.getMessage());
    }
    
    @Test
    void testPrivateConstructor() throws Exception {
        // Use reflection to access the private constructor
        var constructor = EnvConfig.class.getDeclaredConstructor();

        // Assert that the constructor is private
        assertTrue((constructor.getModifiers() & java.lang.reflect.Modifier.PRIVATE) != 0, "Constructor is not private");

        // Make the constructor accessible and invoke it to test behavior
        constructor.setAccessible(true);
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        // Verify the cause of the exception
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof UnsupportedOperationException, "Unexpected exception type thrown");
        assertEquals("This is a utility class and cannot be instantiated", cause.getMessage());
    }


}

