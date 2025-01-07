package com.uapa.software;

import com.uapa.software.controllers.UserController;
import com.uapa.software.entities.User;
import com.uapa.software.views.LoginView;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class LoginViewTest {

    private LoginView loginView;
    private UserController mockController;

    @BeforeAll
    static void configureHeadless() {
        System.setProperty("env", "test");
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Running in headless mode, skipping GUI initialization...");
            System.setProperty("java.awt.headless", "true");
        } else {
            System.setProperty("java.awt.headless", "false");
        }
    }

    @BeforeEach
    void setUp() {
        if (!GraphicsEnvironment.isHeadless()) {
            loginView = new LoginView();
            mockController = mock(UserController.class);
            loginView.setUserController(mockController); // Inject mock
            loginView.setVisible(true);
        }
    }

    @Test
    void testFrameInitialization() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Test skipped in headless mode: testFrameInitialization");
            return;
        }

        assertThat(loginView.getTitle()).isEqualTo("Login View");
        assertThat(loginView.getDefaultCloseOperation()).isEqualTo(WindowConstants.EXIT_ON_CLOSE);
        assertThat(loginView.isResizable()).isFalse();
        assertThat(loginView.getSize()).isEqualTo(new Dimension(600, 400));
        assertThat(loginView.isVisible()).isTrue();
    }

    @Test
    void testUIComponents() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Test skipped in headless mode: testUIComponents");
            return;
        }

        JTextField txtUserName = findComponentByType(loginView.getContentPane(), JTextField.class);
        JPasswordField txtUserPassword = findComponentByType(loginView.getContentPane(), JPasswordField.class);
        JButton btnLogin = findComponentByText(loginView.getContentPane(), JButton.class, "Login");
        JButton btnSignup = findComponentByText(loginView.getContentPane(), JButton.class, "Signup");
        JButton btnForgotPassword = findComponentByText(loginView.getContentPane(), JButton.class, "Forgot Password");

        assertThat(txtUserName).isNotNull();
        assertThat(txtUserPassword).isNotNull();
        assertThat(btnLogin).isNotNull();
        assertThat(btnSignup).isNotNull();
        assertThat(btnForgotPassword).isNotNull();
    }

    @Test
    void testHandleLoginSuccess() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Test skipped in headless mode: testHandleLoginSuccess");
            return;
        }

        // Set up mock behavior for successful login
        when(mockController.login(any(User.class))).thenReturn("mockToken");

        // Simulate user input
        JTextField txtUserName = findComponentByType(loginView.getContentPane(), JTextField.class);
        JPasswordField txtUserPassword = findComponentByType(loginView.getContentPane(), JPasswordField.class);

        txtUserName.setText("testuser");
        txtUserPassword.setText("password");

        // Simulate login button click
        JButton btnLogin = findComponentByText(loginView.getContentPane(), JButton.class, "Login");
        btnLogin.doClick();

        // Verify interaction with the controller
        verify(mockController).login(argThat(user -> user.getUsername().equals("testuser") &&
                user.getPassword().equals("password")));

        // Additional assertions for UI behavior (e.g., opening HomeView) can be added
        // with further mocking or stubbing.
    }

    @Test
    void testHandleLoginFailure() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Test skipped in headless mode: testHandleLoginFailure");
            return;
        }

        // Set up mock behavior for failed login
        when(mockController.login(any(User.class))).thenReturn(null);

        // Simulate user input
        JTextField txtUserName = findComponentByType(loginView.getContentPane(), JTextField.class);
        JPasswordField txtUserPassword = findComponentByType(loginView.getContentPane(), JPasswordField.class);

        txtUserName.setText("testuser");
        txtUserPassword.setText("wrongpassword");

        // Simulate login button click
        JButton btnLogin = findComponentByText(loginView.getContentPane(), JButton.class, "Login");
        btnLogin.doClick();

        // Verify interaction with the controller
        verify(mockController).login(argThat(user -> user.getUsername().equals("testuser") &&
                user.getPassword().equals("wrongpassword")));

        // Assert that error dialog is shown
        // Use JOptionPane testing stubs or mocking frameworks to verify this
    }

    @Test
    void testHandleRegistrationTransition() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Test skipped in headless mode: testHandleRegistrationTransition");
            return;
        }

        // Simulate signup button click
        JButton btnSignup = findComponentByText(loginView.getContentPane(), JButton.class, "Signup");
        btnSignup.doClick();

        // Verify transition behavior (e.g., opening RegistrationView)
        // Mock or stub the RegistrationView to verify instantiation or visibility
    }

    // Utility method to find components by type
    private <T extends Component> T findComponentByType(Container container, Class<T> type) {
        for (Component component : container.getComponents()) {
            if (type.isInstance(component)) {
                return type.cast(component);
            } else if (component instanceof Container) {
                T result = findComponentByType((Container) component, type);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    // Utility method to find components by text
    private <T extends Component> T findComponentByText(Container container, Class<T> type, String text) {
        for (Component component : container.getComponents()) {
            if (type.isInstance(component) && type.cast(component).toString().contains(text)) {
                return type.cast(component);
            } else if (component instanceof Container) {
                T result = findComponentByText((Container) component, type, text);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
