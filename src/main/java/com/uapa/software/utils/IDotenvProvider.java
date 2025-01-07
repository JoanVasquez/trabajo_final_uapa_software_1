package com.uapa.software.utils;

import io.github.cdimascio.dotenv.Dotenv;

public interface IDotenvProvider {
    Dotenv getDotenv(String environment);
}
