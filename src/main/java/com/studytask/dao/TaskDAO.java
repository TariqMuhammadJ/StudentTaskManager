package com.studytask.dao;

import com.studytask.models.Task;
import com.studytask.exceptions.DAOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskDAO extends GenericDAO<Task> {

    @Override
    public Optional<Task> findById(int id) throws DAOException {
        String sql = "SELECT * FROM Tasks WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapResultSetToTask(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DAOException("Error finding task by ID", e);
        }
    }

    @Override
    public List<Task> findAll() throws DAOException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM Tasks ORDER BY due_date ASC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tasks.add(mapResultSetToTask(rs));
            }
            return tasks;
        } catch (SQLException e) {
            throw new DAOException("Error retrieving all tasks", e);
        }
    }

    @Override
    public void save(Task task) throws DAOException {
        String sql = "INSERT INTO Tasks (title, description, due_date, completed, user_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setTaskParameters(stmt, task);
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    task.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error creating task", e);
        }
    }

    @Override
    public void update(Task task) throws DAOException {
        String sql = "UPDATE Tasks SET title = ?, description = ?, due_date = ?, completed = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setTaskParameters(stmt, task);
            stmt.setInt(5, task.getId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("Task not found with ID: " + task.getId());
            }
        } catch (SQLException e) {
            throw new DAOException("Error updating task", e);
        }
    }

    @Override
    public void delete(int id) throws DAOException {
        String sql = "DELETE FROM Tasks WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("Task not found with ID: " + id);
            }
        } catch (SQLException e) {
            throw new DAOException("Error deleting task", e);
        }
    }

    public List<Task> findByUserId(int userId) throws DAOException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM Tasks WHERE user_id = ? ORDER BY due_date ASC";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapResultSetToTask(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error finding tasks for user", e);
        }
        return tasks;
    }
    
    
    private Task mapResultSetToTask(ResultSet rs) throws SQLException {
        return new Task(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getDate("due_date"),
            rs.getBoolean("completed"),
            rs.getInt("user_id")
        );
    }

    private void setTaskParameters(PreparedStatement stmt, Task task) throws SQLException {
        stmt.setString(1, task.getTitle());
        stmt.setString(2, task.getDescription());
        stmt.setDate(3, task.getDueDate());
        stmt.setBoolean(4, task.isCompleted());
        stmt.setInt(5, task.getUserId());
    }

    public List<Task> findCompletedByUserId(int userId) throws DAOException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM Tasks WHERE user_id = ? AND completed = true ORDER BY due_date DESC";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapResultSetToTask(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error finding completed tasks for user", e);
        }
        return tasks;
    }

    public List<Task> findPendingByUserId(int userId) throws DAOException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM Tasks WHERE user_id = ? AND completed = false ORDER BY due_date ASC";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapResultSetToTask(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error finding pending tasks for user", e);
        }
        return tasks;
    }
}