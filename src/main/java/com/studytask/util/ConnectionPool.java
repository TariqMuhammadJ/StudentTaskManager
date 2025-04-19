package com.studytask.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletContext;

public class ConnectionPool {
    private static final int POOL_SIZE = 10;
    private static List<Connection> availableConnections = new ArrayList<>();
    private static List<Connection> usedConnections = new ArrayList<>();
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;
    private static boolean initialized = false;

    public static void init(ServletContext context) {
        // Set MySQL connection properties
        DB_URL = "jdbc:mysql://127.0.0.1:3306/Users?useSSL=false&serverTimezone=UTC";
        DB_USER = "root";
        DB_PASSWORD = ""; // Set your MySQL root password here
        System.out.println("MySQL Database URL: " + DB_URL);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver registered");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            throw new RuntimeException("JDBC Driver not found", e);
        }
    }

    private static synchronized void initializePool() {
        if (!initialized) {
            System.out.println("Initializing MySQL connection pool...");
            try {
                for (int i = 0; i < POOL_SIZE; i++) {
                    Connection conn = createConnection();
                    availableConnections.add(conn);
                    System.out.println("Created connection " + (i + 1) + " of " + POOL_SIZE);
                }
                initialized = true;
                System.out.println("MySQL connection pool initialized successfully");
            } catch (SQLException e) {
                System.err.println("Failed to initialize connection pool: " + e.getMessage());
                throw new RuntimeException("Connection pool initialization failed", e);
            }
        }
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (!initialized) {
            initializePool();
        }

        while (availableConnections.isEmpty()) {
            if (usedConnections.size() < POOL_SIZE) {
                availableConnections.add(createConnection());
                System.out.println("Created new connection as pool was empty");
            } else {
                List<Connection> invalidConnections = new ArrayList<>();
                for (Connection conn : usedConnections) {
                    try {
                        if (!conn.isValid(1)) {
                            invalidConnections.add(conn);
                        }
                    } catch (SQLException e) {
                        invalidConnections.add(conn);
                    }
                }

                for (Connection invalid : invalidConnections) {
                    usedConnections.remove(invalid);
                    try {
                        invalid.close();
                    } catch (SQLException e) {
                        // Ignore
                    }
                }

                if (invalidConnections.isEmpty()) {
                    throw new SQLException("Maximum pool size reached, no available connections!");
                }
                availableConnections.add(createConnection());
            }
        }

        Connection conn = availableConnections.remove(0);
        usedConnections.add(conn);
        return conn;
    }

    public static synchronized void releaseConnection(Connection conn) {
        if (conn != null && usedConnections.remove(conn)) {
            try {
                if (conn.isValid(1)) {
                    availableConnections.add(conn);
                    System.out.println("Connection released back to pool. Available: " + availableConnections.size());
                    return;
                }
            } catch (SQLException e) {
                // Invalid connection
            }

            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    private static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static synchronized void closeAll() {
        System.out.println("Closing all MySQL database connections...");

        for (Connection conn : new ArrayList<>(usedConnections)) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing used connection: " + e.getMessage());
            }
        }
        usedConnections.clear();

        for (Connection conn : new ArrayList<>(availableConnections)) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing available connection: " + e.getMessage());
            }
        }
        availableConnections.clear();

        initialized = false;
        System.out.println("All MySQL connections closed");
    }

    public static synchronized int getAvailableConnectionCount() {
        return availableConnections.size();
    }

    public static synchronized int getUsedConnectionCount() {
        return usedConnections.size();
    }
}
