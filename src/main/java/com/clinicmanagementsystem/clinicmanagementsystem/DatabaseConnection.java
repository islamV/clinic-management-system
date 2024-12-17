package com.clinicmanagementsystem.clinicmanagementsystem;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    static Dotenv env = Dotenv.load();
    private static final String DB_URL = env.get("DB_URL");
    private static final String DB_USERNAME = env.get("DB_USERNAME");
    private static final String DB_PASSWORD = env.get("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }
}
