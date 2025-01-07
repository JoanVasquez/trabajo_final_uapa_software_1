package com.uapa.software.services;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmForgotPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ForgotPasswordRequest;

public class CognitoService {

	private static final Logger logger = LoggerFactory.getLogger(CognitoService.class);

	private final CognitoIdentityProviderClient cognitoClient;
	private final String userPoolId;
	private final String clientId;

	public CognitoService(CognitoIdentityProviderClient cognitoClient, String userPoolId, String clientId) {
		this.cognitoClient = cognitoClient;
		this.userPoolId = userPoolId;
		this.clientId = clientId;
	}

	public CognitoIdentityProviderClient getCognitoClient() {
		return cognitoClient;
	}

	public String registerUser(String username, String password) {
		try {
			AdminCreateUserRequest request = AdminCreateUserRequest.builder()
					.userPoolId(userPoolId)
					.username(username)
					.temporaryPassword(password)
					.userAttributes(AttributeType.builder().name("email_verified").value("true").build())
					.build();

			AdminCreateUserResponse response = cognitoClient.adminCreateUser(request);
			logger.info("User {} registered in Cognito.", username);
			return response.user().username();
		} catch (CognitoIdentityProviderException ex) {
			logger.error("Error registering user: {}", ex.awsErrorDetails().errorMessage(), ex);
			throw new RuntimeException("Error registering user in Cognito", ex);
		}
	}

	/**
	 * Authenticate a user with username and password.
	 *
	 * @param username the username of the user
	 * @param password the password of the user
	 * @return a valid authentication token if successful
	 */
	public String authenticate(String username, String password) {
		try {
			AdminInitiateAuthRequest authRequest = AdminInitiateAuthRequest.builder()
					.authFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
					.userPoolId(userPoolId)
					.clientId(clientId)
					.authParameters(
							Map.of(
									"USERNAME", username,
									"PASSWORD", password))
					.build();

			AdminInitiateAuthResponse response = cognitoClient.adminInitiateAuth(authRequest);

			String token = response.authenticationResult().idToken();
			logger.info("User {} authenticated successfully.", username);
			return token;
		} catch (CognitoIdentityProviderException ex) {
			logger.error("Authentication failed for user {}: {}", username, ex.awsErrorDetails().errorMessage());
			throw new RuntimeException("Authentication failed", ex);
		}
	}

	/**
	 * Initiate the forgot password process for a user.
	 *
	 * @param username the username of the user
	 */
	public void forgotPassword(String username) {
		try {
			ForgotPasswordRequest forgotPasswordRequest = ForgotPasswordRequest.builder()
					.clientId(clientId)
					.username(username)
					.build();

			cognitoClient.forgotPassword(forgotPasswordRequest);
			logger.info("Forgot password process initiated for user {}", username);
		} catch (CognitoIdentityProviderException ex) {
			logger.error("Failed to initiate forgot password for user {}: {}", username,
					ex.awsErrorDetails().errorMessage());
			throw new RuntimeException("Forgot password process failed", ex);
		}
	}

	/**
	 * Confirm a new password after receiving the verification code.
	 *
	 * @param username         the username of the user
	 * @param newPassword      the new password for the user
	 * @param confirmationCode the verification code sent to the user
	 */
	public void confirmNewPassword(String username, String newPassword, String confirmationCode) {
		try {
			ConfirmForgotPasswordRequest confirmRequest = ConfirmForgotPasswordRequest.builder()
					.clientId(clientId)
					.username(username)
					.password(newPassword)
					.confirmationCode(confirmationCode)
					.build();

			cognitoClient.confirmForgotPassword(confirmRequest);
			logger.info("Password reset successfully for user {}", username);
		} catch (CognitoIdentityProviderException ex) {
			logger.error("Failed to reset password for user {}: {}", username, ex.awsErrorDetails().errorMessage());
			throw new RuntimeException("Password reset failed", ex);
		}
	}

	public void closeCognitoClient() {
		this.cognitoClient.close();
	}
}
