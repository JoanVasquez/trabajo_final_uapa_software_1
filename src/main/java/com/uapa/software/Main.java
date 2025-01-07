package com.uapa.software;

import javax.swing.SwingUtilities;

import com.uapa.software.views.LoginView;

public class Main {

    public static void main(String[] args) {
        setEnvironment();
        launchApplication();
    }

    static void setEnvironment() {
        System.setProperty("env", "local");
    }

    static void launchApplication() {
        SwingUtilities.invokeLater(() -> {
            LoginView login = new LoginView();
            login.setVisible(true);
        });
    }
}
