package com.studytask.listeners;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.studytask.util.ConnectionPool;
import com.studytask.util.DatabaseInitializer;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class DatabaseContextListener implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // Initialize the database path first
            ConnectionPool.init(sce.getServletContext());
            
            // Register the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver registered");
            
            // Initialize the database after loading the driver
            DatabaseInitializer.initDatabase();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load MySQL driver", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Deregister JDBC drivers to prevent memory leaks
        try {
            // Unregister all JDBC drivers
            DriverManager.deregisterDriver(new com.mysql.cj.jdbc.Driver());
            System.out.println("MySQL JDBC Driver deregistered");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Shut down MySQL background cleanup thread (if used)
        try {
            // If using MySQL JDBC driver with abandoned connection cleanup
            com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
            System.out.println("MySQL Abandoned Connection Cleanup thread stopped");
        } catch (Exception e) {
            e.printStackTrace();  // Catching general Exception instead of InterruptedException
        }

        // Close all connections in the connection pool
        ConnectionPool.closeAll();
    }
}

