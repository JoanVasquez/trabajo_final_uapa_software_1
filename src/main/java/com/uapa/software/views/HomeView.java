package com.uapa.software.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class HomeView extends JFrame {
    public HomeView() {
        // Set up the JFrame
        setTitle("Customer Relationship Management - Homepage");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Start maximized
        setLayout(new BorderLayout());

        // Create header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180)); // Steel blue color
        headerPanel.setPreferredSize(new Dimension(100, 60));
        JLabel headerLabel = new JLabel("Customer Relationship Management");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Create navigation panel
        JPanel navPanel = new JPanel();
        navPanel.setBackground(new Color(255, 255, 255));
        navPanel.setLayout(new GridLayout(0, 1, 10, 10));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add navigation buttons
        JButton customersButton = createNavButton("Manage Customers");
        JButton billsButton = createNavButton("View Bills");
        JButton productsButton = createNavButton("Manage Products");
        productsButton.addActionListener(this::handleManageProducts);
        JButton interactionsButton = createNavButton("View Interactions");
        JButton preferencesButton = createNavButton("View Preferences");
        JButton logoutButton = createNavButton("Logout");

        navPanel.add(customersButton);
        navPanel.add(billsButton);
        navPanel.add(productsButton);
        navPanel.add(interactionsButton);
        navPanel.add(preferencesButton);
        navPanel.add(logoutButton);

        // Create content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        JLabel contentLabel = new JLabel("Welcome to the CRM system", JLabel.CENTER);
        contentLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        contentPanel.add(contentLabel, BorderLayout.CENTER);

        // Add components to JFrame
        add(headerPanel, BorderLayout.NORTH);
        add(navPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Make the JFrame visible
        setVisible(true);
    }

    // Utility method to create navigation buttons
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setPreferredSize(new Dimension(200, 40));
        button.setFocusPainted(false);
        return button;
    }

    public void handleManageProducts(ActionEvent event) {
        new ManageProductsView();
    }
}
