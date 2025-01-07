package com.uapa.software.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.uapa.software.controllers.UserController;
import com.uapa.software.entities.User;

public class LoginView extends JFrame {

    private JTextField txtUserName;
    private JPasswordField txtUserPassword;
    private JButton btnLogin;
    private JButton btnSignup;
    private JButton btnForgotPassword;
    private UserController userController = new UserController();

    public LoginView() {
        initializeUI();
    }

    private void initializeUI() {
        // Create the JFrame
        this.setTitle("Login View");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(600, 400);
        // this.setLocationRelativeTo(null);
        this.setResizable(false); // Disable maximization

        // Create the main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Create the header label
        JLabel headerLabel = new JLabel("Welcome! Please Login", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Create the form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Username label and text field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        txtUserName = new JTextField(15);
        formPanel.add(txtUserName, gbc);

        // Password label and password field
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        txtUserPassword = new JPasswordField(15);
        formPanel.add(txtUserPassword, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Create the button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        // Login button
        btnLogin = new JButton("Login");
        btnLogin.setPreferredSize(new Dimension(100, 30));
        btnLogin.addActionListener(this::handleLogin);
        buttonPanel.add(btnLogin);

        // Signup button
        btnSignup = new JButton("Signup");
        btnSignup.setPreferredSize(new Dimension(100, 30));
        btnSignup.addActionListener(this::handleRegistration);
        buttonPanel.add(btnSignup);

        // Forgot Password button
        btnForgotPassword = new JButton("Forgot Password");
        btnForgotPassword.setPreferredSize(new Dimension(150, 30));
        buttonPanel.add(btnForgotPassword);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        this.add(mainPanel);
    }

    public void handleLogin(ActionEvent event) {
        User user = new User();
        user.setUsername(this.txtUserName.getText());
        user.setPassword(String.valueOf(this.txtUserPassword.getPassword()));
        String token = userController.login(user);

        if (token != null) {
            dispose(); // Close the login view when successful login are done.
            new HomeView(); // Open the main view after successful login.
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleRegistration(ActionEvent event) {
        new RegistrationView().setVisible(true);
        this.setVisible(false);
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }
}
