package com.uapa.software;

import com.uapa.software.services.CognitoService;
import com.uapa.software.services.FakeCognitoService;
import com.uapa.software.utils.CognitoServiceFactory;
import com.uapa.software.utils.EnvConfig;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CognitoServiceFactoryTest {

    @Test
    void testCreateCognitoService_LocalEnvironment_ReturnsFakeCognitoService() {
        System.setProperty("env", "test");
        try (MockedStatic<EnvConfig> envConfigMock = mockStatic(EnvConfig.class)) {
            envConfigMock.when(() -> EnvConfig.get(anyString())).thenReturn(null);

            CognitoService cognitoService = CognitoServiceFactory.createCognitoService();

            assertTrue(cognitoService instanceof FakeCognitoService);
        }
    }

    @Test
    void testCreateCognitoService_TestEnvironment_ReturnsCognitoService() {
        System.setProperty("env", "prod");
        try (MockedStatic<EnvConfig> envConfigMock = mockStatic(EnvConfig.class)) {
            envConfigMock.when(() -> EnvConfig.get("AWS_ACCESS_KEY_ID")).thenReturn("mock_access_key");
            envConfigMock.when(() -> EnvConfig.get("AWS_SECRET_ACCESS_KEY")).thenReturn("mock_secret_key");
            envConfigMock.when(() -> EnvConfig.getOrDefault("AWS_REGION", "us-east-1")).thenReturn("us-east-1");
            envConfigMock.when(() -> EnvConfig.get("COGNITO_USER_POOL_ID")).thenReturn("mock_user_pool_id");
            envConfigMock.when(() -> EnvConfig.get("COGNITO_CLIENT_ID")).thenReturn("mock_client_id");

            CognitoService cognitoService = CognitoServiceFactory.createCognitoService();

            assertNotNull(cognitoService);
            assertFalse(cognitoService instanceof FakeCognitoService);

            // Additional assertions to verify Cognito client initialization
            CognitoIdentityProviderClient cognitoClient = cognitoService.getCognitoClient();
            assertNotNull(cognitoClient);
        }
    }

    @Test
    void testCreateInstanceOfCognitoServiceFactory() {
        // Create an instance of CognitoServiceFactory
        CognitoServiceFactory cognitoServiceFactory = new CognitoServiceFactory();

        // Assert that the instance is not null
        assertNotNull(cognitoServiceFactory);
    }
}
