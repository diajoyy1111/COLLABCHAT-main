package com.collabchat.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    // Read configuration from environment variables with sensible defaults
    // Allow public key retrieval for environments where the server requires it (useful for local setups)
    private static final String URL = System.getenv().getOrDefault("COLLABCHAT_DB_URL", "jdbc:mysql://localhost:3306/collabchat?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC");
    private static final String USER = System.getenv().getOrDefault("COLLABCHAT_DB_USER", "appuser");
    private static final String PASS = System.getenv().getOrDefault("COLLABCHAT_DB_PASS", "AppPass123");

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
