package com.uapa.software.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManageProductsView extends JFrame {

    public ManageProductsView() {
        // Set up JFrame
        setTitle("Manage Products");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180)); // Steel blue color
        headerPanel.setPreferredSize(new Dimension(100, 60));
        JLabel headerLabel = new JLabel("Manage Products");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Table panel
        String[] columnNames = { "ID", "Name", "Description", "Price" };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable productsTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(productsTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Footer panel with buttons
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton addButton = new JButton("Add Product");
        JButton editButton = new JButton("Edit Product");
        JButton deleteButton = new JButton("Delete Product");

        addButton.setPreferredSize(new Dimension(120, 30));
        editButton.setPreferredSize(new Dimension(120, 30));
        deleteButton.setPreferredSize(new Dimension(120, 30));

        footerPanel.add(addButton);
        footerPanel.add(editButton);
        footerPanel.add(deleteButton);

        add(footerPanel, BorderLayout.SOUTH);

        // Action listeners for buttons (placeholder logic)
        addButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Add Product clicked!"));
        editButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Edit Product clicked!"));
        deleteButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Delete Product clicked!"));

        // Populate table with sample data (replace with database integration)
        populateTableWithSampleData(tableModel);

        // Make JFrame visible
        setVisible(true);
    }

    private void populateTableWithSampleData(DefaultTableModel tableModel) {
        tableModel.addRow(new Object[] { 1, "Product A", "Description of Product A", 10.99 });
        tableModel.addRow(new Object[] { 2, "Product B", "Description of Product B", 15.49 });
        tableModel.addRow(new Object[] { 3, "Product C", "Description of Product C", 7.25 });
    }

}
