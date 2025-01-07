package com.uapa.software;

import com.uapa.software.controllers.UserController;
import com.uapa.software.entities.User;
import com.uapa.software.views.RegistrationView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class RegistrationViewTest {

    private RegistrationView registrationView;
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
            registrationView = new RegistrationView();
            mockController = mock(UserController.class);
            registrationView.setUserController(mockController);
            registrationView.setVisible(true);
        }
    }

    @Test
    void testFrameInitialization() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Test skipped in headless mode: testFrameInitialization");
            return;
        }

        assertThat(registrationView.getTitle()).isEqualTo("User Registration");
        assertThat(registrationView.getDefaultCloseOperation()).isEqualTo(WindowConstants.EXIT_ON_CLOSE);
        assertThat(registrationView.getLayout()).isInstanceOf(BorderLayout.class);
        assertThat(registrationView.isVisible()).isTrue();
    }

    @Test
    void testFormPanelComponents() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Test skipped in headless mode: testFormPanelComponents");
            return;
        }

        JPanel formPanel = (JPanel) registrationView.getContentPane().getComponent(1);
        assertThat(formPanel.getLayout()).isInstanceOf(GridBagLayout.class);

        JTextField txtUsername = findComponentByType(formPanel, JTextField.class);
        assertThat(txtUsername).isNotNull();

        JPasswordField txtPassword = findComponentByType(formPanel, JPasswordField.class);
        assertThat(txtPassword).isNotNull();

        JComboBox<String> cbRoles = findComponentByType(formPanel, JComboBox.class);
        assertThat(cbRoles).isNotNull();
        assertThat(cbRoles.getItemCount()).isEqualTo(3);
    }

    @Test
    void testHandleRegistrationSuccess() {
        if (GraphicsEnvironment.isHeadless())
            return;

        // Set up mock behavior for successful registration
        when(mockController.saveAndLogin(any(User.class))).thenReturn("mockToken");

        // Simulate user input
        JTextField txtUsername = findComponentByType(registrationView.getContentPane(), JTextField.class);
        JPasswordField txtPassword = findComponentByType(registrationView.getContentPane(), JPasswordField.class);
        JComboBox<String> cbRoles = findComponentByType(registrationView.getContentPane(), JComboBox.class);

        txtUsername.setText("testuser");
        txtPassword.setText("password");
        cbRoles.setSelectedIndex(1); // "User"

        // Simulate button click
        registrationView.handleRegistration(null);

        // Verify interaction with the controller
        verify(mockController).saveAndLogin(argThat(user -> user.getUsername().equals("testuser") &&
                user.getPassword().equals("password") &&
                user.getRol().equals("User")));

        // Verify fields are cleared
        assertThat(txtUsername.getText()).isEmpty();
        assertThat(txtPassword.getPassword()).isEmpty();
        assertThat(cbRoles.getSelectedIndex()).isZero();

        // Verify HomeView is displayed
        // This requires HomeView to be testable
    }

    @Test
    void testHandleRegistrationFailure() {
        if (GraphicsEnvironment.isHeadless())
            return;

        // Set up mock behavior for failed registration
        when(mockController.saveAndLogin(any(User.class))).thenReturn(null);

        // Simulate user input
        JTextField txtUsername = findComponentByType(registrationView.getContentPane(), JTextField.class);
        JPasswordField txtPassword = findComponentByType(registrationView.getContentPane(), JPasswordField.class);
        JComboBox<String> cbRoles = findComponentByType(registrationView.getContentPane(), JComboBox.class);

        txtUsername.setText("testuser");
        txtPassword.setText("password");
        cbRoles.setSelectedIndex(1); // "User"

        // Simulate button click
        registrationView.handleRegistration(null);

        // Verify interaction with the controller
        verify(mockController).saveAndLogin(argThat(user -> user.getUsername().equals("testuser") &&
                user.getPassword().equals("password") &&
                user.getRol().equals("User")));

        // Verify fields are cleared
        assertThat(txtUsername.getText()).isEmpty();
        assertThat(txtPassword.getPassword()).isEmpty();
        assertThat(cbRoles.getSelectedIndex()).isZero();

        // Verify HomeView is NOT displayed
        // This requires HomeView to be mockable or testable
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
}
