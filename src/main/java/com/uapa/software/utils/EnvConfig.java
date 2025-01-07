package com.uapa.software.utils;

import io.github.cdimascio.dotenv.Dotenv;

public final class EnvConfig {

    private static Dotenv dotenv;
    private static IDotenvProvider dotenvProvider = new DefaultDotenvProvider();

    private EnvConfig() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void resetDotenv() {
        dotenv = null;
    }

    public static void setDotenvProvider(IDotenvProvider provider) {
        dotenvProvider = provider;
    }

    private static Dotenv getDotenv() {
        if (dotenv == null) {
            String environment = System.getProperty("env", "default");
            dotenv = dotenvProvider.getDotenv(environment);
        }
        return dotenv;
    }

    public static String get(String key) {
        String value = getDotenv().get(key);
        if (value == null) {
            throw new IllegalArgumentException("Environment variable '" + key + "' is not defined");
        }
        return value;
    }

    public static String getOrDefault(String key, String defaultValue) {
        return getDotenv().get(key, defaultValue);
    }
}
