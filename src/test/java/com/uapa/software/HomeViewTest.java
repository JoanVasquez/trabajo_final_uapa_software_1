package com.uapa.software;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uapa.software.views.HomeView;

import javax.swing.*;
import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;

class HomeViewTest {

	private HomeView homeView;

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
		// Check if the environment is headless
		if (GraphicsEnvironment.isHeadless()) {
			System.out.println("Running in headless mode, skipping GUI initialization...");
			homeView = null; // Do not initialize in headless mode
		} else {
			homeView = new HomeView(); // Initialize normally
		}
	}

	@Test
	void testFrameInitialization() {
		if (GraphicsEnvironment.isHeadless()) {
			System.out.println("Test skipped in headless mode: testFrameInitialization");
			return;
		}

		assertThat(homeView.getTitle()).isEqualTo("Customer Relationship Management - Homepage");
		assertThat(homeView.getDefaultCloseOperation()).isEqualTo(JFrame.EXIT_ON_CLOSE);
		assertThat(homeView.getLayout()).isInstanceOf(BorderLayout.class);
		assertThat(homeView.isVisible()).isTrue();
	}

	@Test
	void testHeaderPanelSetup() {
		if (GraphicsEnvironment.isHeadless()) {
			System.out.println("Test skipped in headless mode: testHeaderPanelSetup");
			return;
		}

		Component headerPanel = homeView.getContentPane().getComponent(0);
		assertThat(headerPanel).isInstanceOf(JPanel.class);

		JPanel panel = (JPanel) headerPanel;
		assertThat(panel.getBackground()).isEqualTo(new Color(70, 130, 180));

		JLabel headerLabel = (JLabel) panel.getComponent(0);
		assertThat(headerLabel.getText()).isEqualTo("Customer Relationship Management");
		assertThat(headerLabel.getFont().getStyle()).isEqualTo(Font.BOLD);
		assertThat(headerLabel.getForeground()).isEqualTo(Color.WHITE);
	}

	@Test
	void testNavPanelSetup() {
		if (GraphicsEnvironment.isHeadless()) {
			System.out.println("Test skipped in headless mode: testNavPanelSetup");
			return;
		}

		Component navPanel = homeView.getContentPane().getComponent(1);
		assertThat(navPanel).isInstanceOf(JPanel.class);

		JPanel panel = (JPanel) navPanel;
		assertThat(panel.getLayout()).isInstanceOf(GridLayout.class);
		assertThat(panel.getComponentCount()).isEqualTo(6);

		String[] expectedButtons = { "Manage Customers", "View Bills", "Manage Products", "View Interactions",
				"View Preferences", "Logout" };

		for (int i = 0; i < expectedButtons.length; i++) {
			JButton button = (JButton) panel.getComponent(i);
			assertThat(button.getText()).isEqualTo(expectedButtons[i]);
		}
	}

	@Test
	void testContentPanelSetup() {
		if (GraphicsEnvironment.isHeadless()) {
			System.out.println("Test skipped in headless mode: testContentPanelSetup");
			return;
		}

		Component contentPanel = homeView.getContentPane().getComponent(2);
		assertThat(contentPanel).isInstanceOf(JPanel.class);

		JPanel panel = (JPanel) contentPanel;
		JLabel contentLabel = (JLabel) panel.getComponent(0);
		assertThat(contentLabel.getText()).isEqualTo("Welcome to the CRM system");
		assertThat(contentLabel.getHorizontalAlignment()).isEqualTo(SwingConstants.CENTER);
	}
}
