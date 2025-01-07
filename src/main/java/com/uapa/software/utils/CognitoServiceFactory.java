package com.uapa.software.utils;

import com.uapa.software.services.CognitoService;
import com.uapa.software.services.FakeCognitoService;

import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;

public class CognitoServiceFactory {

    public static CognitoService createCognitoService() {

        String environment = System.getProperty("env", "local");

        if ("local".equals(environment) || "test".equals(environment)) {
            return new FakeCognitoService();
        }

        // Load configuration from environment
        String accessKey = EnvConfig.get("AWS_ACCESS_KEY_ID");
        String secretKey = EnvConfig.get("AWS_SECRET_ACCESS_KEY");
        String region = EnvConfig.getOrDefault("AWS_REGION", "us-east-1");
        String userPoolId = EnvConfig.get("COGNITO_USER_POOL_ID");
        String clientId = EnvConfig.get("COGNITO_CLIENT_ID");

        // Create AWS credentials
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);

        // Initialize CognitoIdentityProviderClient
        CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();

        // Create and return the CognitoService instance
        return new CognitoService(cognitoClient, userPoolId, clientId);
    }

}
