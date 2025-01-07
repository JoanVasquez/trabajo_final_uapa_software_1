package com.uapa.software;

import org.junit.jupiter.api.Test;

import com.uapa.software.services.FakeCognitoService;

import static org.junit.jupiter.api.Assertions.*;

public class FakeCognitoServiceTest {

    @Test
    void testRegisterUser() {
        // Arrange
        FakeCognitoService fakeCognitoService = new FakeCognitoService();
        String username = "testuser";
        String password = "testpassword";

        // Act
        String registeredUser = fakeCognitoService.registerUser(username, password);

        // Assert
        assertNotNull(registeredUser, "Registered user should not be null");
        assertEquals(username, registeredUser, "The returned username should match the input username");
    }

    @Test
    void testAuthenticate() {
        // Arrange
        FakeCognitoService fakeCognitoService = new FakeCognitoService();
        String username = "testuser";
        String password = "testpassword";

        // Act
        String jwtToken = fakeCognitoService.authenticate(username, password);

        // Assert
        assertNotNull(jwtToken, "JWT token should not be null");
        assertEquals("mock-jwt-token", jwtToken, "JWT token should match the mock response");
    }

    @Test
    void testForgotPassword() {
        // Arrange
        FakeCognitoService fakeCognitoService = new FakeCognitoService();
        String username = "testuser";

        // Act & Assert
        assertDoesNotThrow(() -> fakeCognitoService.forgotPassword(username),
                "Forgot password should not throw an exception");
    }

    @Test
    void testConfirmNewPassword() {
        // Arrange
        FakeCognitoService fakeCognitoService = new FakeCognitoService();
        String username = "testuser";
        String newPassword = "newpassword";
        String confirmationCode = "12345";

        // Act & Assert
        assertDoesNotThrow(() -> fakeCognitoService.confirmNewPassword(username, newPassword, confirmationCode),
                "Confirm new password should not throw an exception");
    }
}