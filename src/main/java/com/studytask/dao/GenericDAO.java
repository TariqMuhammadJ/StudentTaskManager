package com.studytask.dao;

import com.studytask.exceptions.DAOException;
import com.studytask.util.ConnectionPool;  // Change this import
import java.sql.Connection;
import java.sql.SQLException;

public abstract class GenericDAO<T> implements BaseDAO<T> {
    
    protected Connection getConnection() throws SQLException {
        return ConnectionPool.getConnection();  // This uses ConnectionPool
    }

    protected void executeTransaction(TransactionExecutor executor) throws DAOException {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            executor.execute(conn);
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new DAOException("Error rolling back transaction", ex);
                }
            }
            throw new DAOException("Transaction failed", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    ConnectionPool.releaseConnection(conn);  // Use releaseConnection
                } catch (SQLException e) {
                    throw new DAOException("Error releasing connection", e);
                }
            }
        }
    }

    @FunctionalInterface
    protected interface TransactionExecutor {
        void execute(Connection conn) throws SQLException;
    }
}