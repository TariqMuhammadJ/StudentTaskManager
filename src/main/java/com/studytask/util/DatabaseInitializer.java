package com.studytask.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initDatabase() {
        try (Connection conn = ConnectionPool.getConnection();
             Statement stmt = conn.createStatement()) {
            
            System.out.println("Starting database initialization...");
            
            // Create Users table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Users (" +
                "id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                "login VARCHAR(50) NOT NULL UNIQUE, " +
                "password VARCHAR(255) NOT NULL, " +
                "isActive BOOLEAN DEFAULT FALSE" +
                ")"
            );
            System.out.println("Users table created successfully");

            // Create Groups table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Groups (" +
                "id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );
            System.out.println("Groups table created successfully");

            // Create Tasks table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Tasks (" +
                "id INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                "title VARCHAR(100) NOT NULL, " +
                "description VARCHAR(500), " +
                "due_date DATE, " +
                "completed BOOLEAN DEFAULT FALSE, " +
                "user_id INTEGER, " +
                "FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE" +
                ")"
            );
            System.out.println("Tasks table created successfully");

            // Create GroupMembers table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS GroupMembers (" +
                "group_id INTEGER, " +
                "user_id INTEGER, " +
                "PRIMARY KEY (group_id, user_id), " +
                "FOREIGN KEY (group_id) REFERENCES Groups(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE" +
                ")"
            );
            System.out.println("GroupMembers table created successfully");

            // Check if test user exists
            try (PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM Users WHERE login = ?")) {
                checkStmt.setString(1, "Oronovo");
                ResultSet rs = checkStmt.executeQuery();
                rs.next();
                int count = rs.getInt(1);
                
                // Create test user if it doesn't exist
                if (count == 0) {
                    try (PreparedStatement insertStmt = conn.prepareStatement(
                            "INSERT INTO Users (login, password) VALUES (?, ?)")) {
                        insertStmt.setString(1, "Oronovo");
                        insertStmt.setString(2, "password123");
                        insertStmt.executeUpdate();
                        System.out.println("Test user created successfully");
                    }
                } else {
                    System.out.println("Test user already exists");
                }
            }
            System.out.println("Database initialization completed successfully");

        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}