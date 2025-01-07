package com.uapa.software;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.GraphicsEnvironment;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.uapa.software.views.LoginView;

class MainTest {

	@BeforeAll
	static void configureHeadless() {
		if (GraphicsEnvironment.isHeadless()) {
			System.out.println("Running in headless mode, skipping GUI initialization...");
			System.setProperty("java.awt.headless", "true");
		} else {
			System.setProperty("java.awt.headless", "false");
		}
	}
	
    @Test
    void testSetEnvironment() {
        // Test the environment setup
        Main.setEnvironment();
        assertEquals("local", System.getProperty("env"), "Environment variable 'env' should be set to 'local'");
    }

    @Test
    void testLaunchApplication() {
        // Simulate the SwingUtilities.invokeLater execution directly
        Runnable appLauncher = () -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true); // Simulate the UI being shown
            assertEquals("Login View", loginView.getTitle(), "The LoginView should have the correct title");
            assertEquals(true, loginView.isVisible(), "LoginView should be visible");
        };

        // Execute the Runnable
        appLauncher.run();
    }

    @Test
    void testMainMethod() {
        // Reset system properties to avoid side effects
        System.clearProperty("env");

        // Call the main method
        Main.main(new String[] {});

        // Verify environment variable
        assertEquals("local", System.getProperty("env"), "Environment variable 'env' should be set to 'local'");
    }

    @Test
    void testMainInstanceCreation() {
        // Create an instance of Main
        Main mainInstance = new Main();

        // Verify the instance is not null
        assertNotNull(mainInstance, "Main instance should be created successfully");
    }

}
