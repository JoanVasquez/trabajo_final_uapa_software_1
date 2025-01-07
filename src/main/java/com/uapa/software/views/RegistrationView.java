package com.uapa.software.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.uapa.software.controllers.UserController;
import com.uapa.software.entities.User;

public class RegistrationView extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cbRoles;
    private UserController userController = new UserController();

    // Constructor
    public RegistrationView() {
        // Set up the JFrame
        setTitle("User Registration");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); // Center the frame
        setLayout(new BorderLayout());

        // Create and add components
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        // Enable resizing adaptation
        setMinimumSize(new Dimension(500, 300));
    }

    // Create the header panel
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(45, 140, 240));
        JLabel titleLabel = new JLabel("User Registration Form");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel);
        return headerPanel;
    }

    // Create the form panel
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        txtUsername = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(txtUsername, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        txtPassword = new JPasswordField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(txtPassword, gbc);

        // Role
        JLabel roleLabel = new JLabel("Role:");
        cbRoles = new JComboBox<>(new String[] { "Admin", "User", "Guest" });
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(roleLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(cbRoles, gbc);

        return formPanel;
    }

    // Create the button panel
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 240, 240));

        JButton registerButton = new JButton("Register");
        JButton clearButton = new JButton("Clear");
        registerButton.setBackground(new Color(0, 123, 255));
        registerButton.setForeground(Color.WHITE);
        clearButton.setBackground(new Color(220, 53, 69));
        clearButton.setForeground(Color.WHITE);

        // Add buttons
        buttonPanel.add(registerButton);
        buttonPanel.add(clearButton);

        // Button Actions (example)
        registerButton.addActionListener(this::handleRegistration);
        clearButton.addActionListener(e -> clearFormFields());

        return buttonPanel;
    }

    public void handleRegistration(ActionEvent event) {
        User user = new User();
        user.setUsername(this.txtUsername.getText());
        user.setPassword(String.valueOf(this.txtPassword.getPassword()));
        user.setRol(String.valueOf(this.cbRoles.getSelectedItem()));

        clearFormFields();

        String token = userController.saveAndLogin(user);

        if (token != null) {
            JOptionPane.showMessageDialog(this, "User registered and logged in successfully", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close the registration view when successful registration and login are done.
            new HomeView(); // Open the main view after successful registration and login.
        } else {
            JOptionPane.showMessageDialog(this, "Failed to register user", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to clear form fields
    private void clearFormFields() {
        for (Component component : getContentPane().getComponents()) {
            if (component instanceof JPanel) {
                for (Component field : ((JPanel) component).getComponents()) {
                    if (field instanceof JTextField) {
                        ((JTextField) field).setText("");
                    } else if (field instanceof JPasswordField) {
                        ((JPasswordField) field).setText("");
                    } else if (field instanceof JComboBox) {
                        ((JComboBox<?>) field).setSelectedIndex(0);
                    }
                }
            }
        }
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }

}
