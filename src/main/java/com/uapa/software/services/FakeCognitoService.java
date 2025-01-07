package com.uapa.software.services;

public class FakeCognitoService extends CognitoService {

    public FakeCognitoService() {
        super(null, "fake-user-pool-id", "fake-client-id");
    }

    @Override
    public String registerUser(String username, String password) {
        System.out.println("Registering user locally: " + username);
        return username; // Mock response
    }

    @Override
    public String authenticate(String username, String password) {
        System.out.println("Authenticating user locally: " + username);
        return "mock-jwt-token"; // Mock response
    }

    @Override
    public void forgotPassword(String username) {
        System.out.println("Forgot password triggered locally for: " + username);
    }

    @Override
    public void confirmNewPassword(String username, String newPassword, String confirmationCode) {
        System.out.println("Password reset locally for: " + username);
    }
}
