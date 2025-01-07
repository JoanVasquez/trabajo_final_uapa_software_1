package com.uapa.software;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uapa.software.views.ManageProductsView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;

class ManageProductsViewTest {

    private ManageProductsView manageProductsView;

    @BeforeAll
    static void configureHeadless() {
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
            manageProductsView = new ManageProductsView();
        }
    }

    @Test
    void testFrameInitialization() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Test skipped in headless mode: testFrameInitialization");
            return;
        }

        assertThat(manageProductsView.getTitle()).isEqualTo("Manage Products");
        assertThat(manageProductsView.getDefaultCloseOperation()).isEqualTo(WindowConstants.DISPOSE_ON_CLOSE);
        assertThat(manageProductsView.getLayout()).isInstanceOf(BorderLayout.class);
        assertThat(manageProductsView.isVisible()).isTrue();
    }

    @Test
    void testHeaderPanelSetup() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Test skipped in headless mode: testHeaderPanelSetup");
            return;
        }

        JPanel headerPanel = (JPanel) manageProductsView.getContentPane().getComponent(0);
        assertThat(headerPanel.getBackground()).isEqualTo(new Color(70, 130, 180));
        assertThat(headerPanel.getPreferredSize()).isEqualTo(new Dimension(100, 60));

        JLabel headerLabel = (JLabel) headerPanel.getComponent(0);
        assertThat(headerLabel.getText()).isEqualTo("Manage Products");
        assertThat(headerLabel.getFont().getStyle()).isEqualTo(Font.BOLD);
        assertThat(headerLabel.getForeground()).isEqualTo(Color.WHITE);
    }

    @Test
    void testTableSetup() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Test skipped in headless mode: testTableSetup");
            return;
        }

        JScrollPane tableScrollPane = (JScrollPane) manageProductsView.getContentPane().getComponent(1);
        JTable productsTable = (JTable) tableScrollPane.getViewport().getView();

        assertThat(productsTable.getColumnCount()).isEqualTo(4);
        assertThat(productsTable.getColumnName(0)).isEqualTo("ID");
        assertThat(productsTable.getColumnName(1)).isEqualTo("Name");
        assertThat(productsTable.getColumnName(2)).isEqualTo("Description");
        assertThat(productsTable.getColumnName(3)).isEqualTo("Price");

        DefaultTableModel tableModel = (DefaultTableModel) productsTable.getModel();
        assertThat(tableModel.getRowCount()).isEqualTo(3); // Sample data
        assertThat(tableModel.getValueAt(0, 1)).isEqualTo("Product A");
    }

    @Test
    void testFooterPanelSetup() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Test skipped in headless mode: testFooterPanelSetup");
            return;
        }

        JPanel footerPanel = (JPanel) manageProductsView.getContentPane().getComponent(2);
        assertThat(footerPanel.getLayout()).isInstanceOf(FlowLayout.class);

        JButton addButton = (JButton) footerPanel.getComponent(0);
        JButton editButton = (JButton) footerPanel.getComponent(1);
        JButton deleteButton = (JButton) footerPanel.getComponent(2);

        assertThat(addButton.getText()).isEqualTo("Add Product");
        assertThat(editButton.getText()).isEqualTo("Edit Product");
        assertThat(deleteButton.getText()).isEqualTo("Delete Product");

        assertThat(addButton.getPreferredSize()).isEqualTo(new Dimension(120, 30));
        assertThat(editButton.getPreferredSize()).isEqualTo(new Dimension(120, 30));
        assertThat(deleteButton.getPreferredSize()).isEqualTo(new Dimension(120, 30));
    }

    @Test
    void testButtonActions() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Test skipped in headless mode: testButtonActions");
            return;
        }

        JPanel footerPanel = (JPanel) manageProductsView.getContentPane().getComponent(2);

        JButton addButton = (JButton) footerPanel.getComponent(0);
        JButton editButton = (JButton) footerPanel.getComponent(1);
        JButton deleteButton = (JButton) footerPanel.getComponent(2);

        // Simulate button clicks and capture messages
        addButton.doClick();
        editButton.doClick();
        deleteButton.doClick();

        // You can use spies or custom dialog boxes to test this further
        // For now, these actions trigger a placeholder JOptionPane
    }
}
