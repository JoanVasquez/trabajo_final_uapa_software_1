package com.uapa.software.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class DefaultDotenvProvider implements IDotenvProvider {

    @Override
    public Dotenv getDotenv(String environment) {
        if ("test".equals(environment) || "local".equals(environment)) {
            return Dotenv.configure().filename(".env.test").load();
        } 
        
        return Dotenv.configure().load();
    }
}
