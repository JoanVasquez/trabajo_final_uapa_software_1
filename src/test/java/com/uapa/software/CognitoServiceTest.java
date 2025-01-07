package com.uapa.software;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.uapa.software.services.CognitoService;

import software.amazon.awssdk.awscore.exception.AwsErrorDetails;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

class CognitoServiceTest {

    @Mock
    private CognitoIdentityProviderClient cognitoClient;

    private CognitoService cognitoService;

    private final String userPoolId = "testUserPoolId";
    private final String clientId = "testClientId";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cognitoService = new CognitoService(cognitoClient, userPoolId, clientId);
    }

    @Test
    void testRegisterUser_success() {
        String username = "testUser";
        String password = "testPassword";

        AdminCreateUserResponse mockResponse = AdminCreateUserResponse.builder()
                .user(UserType.builder().username(username).build())
                .build();

        when(cognitoClient.adminCreateUser(any(AdminCreateUserRequest.class))).thenReturn(mockResponse);

        String result = cognitoService.registerUser(username, password);

        assertEquals(username, result);
        verify(cognitoClient, times(1)).adminCreateUser(any(AdminCreateUserRequest.class));
    }

    @Test
    void testRegisterUser_failure() {
        String username = "testUser";
        String password = "testPassword";

        CognitoIdentityProviderException exception = mock(CognitoIdentityProviderException.class);
        when(exception.awsErrorDetails()).thenReturn(
                AwsErrorDetails.builder().errorMessage("Mocked error message").build());

        // Mock the behavior to throw this exception
        when(cognitoClient.adminCreateUser(any(AdminCreateUserRequest.class))).thenThrow(exception);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> cognitoService.registerUser(username, password));

        assertEquals("Error registering user in Cognito", ex.getMessage());
        verify(cognitoClient, times(1)).adminCreateUser(any(AdminCreateUserRequest.class));
    }

    @Test
    void testConfirmNewPassword_success() {
        String username = "testUser";
        String newPassword = "newPassword";
        String confirmationCode = "12345";

        when(cognitoClient.confirmForgotPassword(any(ConfirmForgotPasswordRequest.class))).thenReturn(null);

        assertDoesNotThrow(() -> cognitoService.confirmNewPassword(username, newPassword, confirmationCode));
        verify(cognitoClient, times(1)).confirmForgotPassword(any(ConfirmForgotPasswordRequest.class));
    }

    @Test
    void testConfirmNewPassword_failure() {
        String username = "testUser";
        String newPassword = "newPassword";
        String confirmationCode = "12345";

        AwsErrorDetails errorDetails = AwsErrorDetails.builder()
                .errorMessage("Mocked error message")
                .build();
        CognitoIdentityProviderException exception = mock(CognitoIdentityProviderException.class);
        when(exception.awsErrorDetails()).thenReturn(
                AwsErrorDetails.builder().errorMessage("Mocked error message").build());

        when(cognitoClient.confirmForgotPassword(any(ConfirmForgotPasswordRequest.class))).thenThrow(exception);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> cognitoService.confirmNewPassword(username, newPassword, confirmationCode));

        assertEquals("Password reset failed", ex.getMessage());
        verify(cognitoClient, times(1)).confirmForgotPassword(any(ConfirmForgotPasswordRequest.class));
    }

    @Test
    void testAuthenticate_success() {
        String username = "testUser";
        String password = "testPassword";
        String token = "testToken";

        AdminInitiateAuthResponse mockResponse = AdminInitiateAuthResponse.builder()
                .authenticationResult(AuthenticationResultType.builder().idToken(token).build())
                .build();

        when(cognitoClient.adminInitiateAuth(any(AdminInitiateAuthRequest.class))).thenReturn(mockResponse);

        String result = cognitoService.authenticate(username, password);

        assertEquals(token, result);
        verify(cognitoClient, times(1)).adminInitiateAuth(any(AdminInitiateAuthRequest.class));
    }

    @Test
    void testAuthenticate_failure() {
        String username = "testUser";
        String password = "testPassword";

        AwsErrorDetails errorDetails = AwsErrorDetails.builder()
                .errorMessage("Mocked error message")
                .build();
        CognitoIdentityProviderException exception = mock(CognitoIdentityProviderException.class);
        when(exception.awsErrorDetails()).thenReturn(errorDetails);

        when(cognitoClient.adminInitiateAuth(any(AdminInitiateAuthRequest.class))).thenThrow(exception);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> cognitoService.authenticate(username, password));

        assertEquals("Authentication failed", ex.getMessage());
        verify(cognitoClient, times(1)).adminInitiateAuth(any(AdminInitiateAuthRequest.class));
    }

    @Test
    void testForgotPassword_success() {
        String username = "testUser";

        when(cognitoClient.forgotPassword(any(ForgotPasswordRequest.class))).thenReturn(null);

        assertDoesNotThrow(() -> cognitoService.forgotPassword(username));
        verify(cognitoClient, times(1)).forgotPassword(any(ForgotPasswordRequest.class));
    }

    @Test
    void testForgotPassword_failure() {
        String username = "testUser";

        AwsErrorDetails errorDetails = AwsErrorDetails.builder()
                .errorMessage("Mocked error message")
                .build();
        CognitoIdentityProviderException exception = mock(CognitoIdentityProviderException.class);
        when(exception.awsErrorDetails()).thenReturn(errorDetails);

        when(cognitoClient.forgotPassword(any(ForgotPasswordRequest.class))).thenThrow(exception);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> cognitoService.forgotPassword(username));

        assertEquals("Forgot password process failed", ex.getMessage());
        verify(cognitoClient, times(1)).forgotPassword(any(ForgotPasswordRequest.class));
    }
    @Test
    void testCloseCognitoClient() {
        doNothing().when(cognitoClient).close();

        assertDoesNotThrow(() -> cognitoService.closeCognitoClient());
        verify(cognitoClient, times(1)).close();
    }
}
