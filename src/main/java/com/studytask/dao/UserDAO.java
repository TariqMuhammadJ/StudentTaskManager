package com.studytask.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.studytask.exceptions.DAOException;
import com.studytask.models.User;

public class UserDAO extends GenericDAO<User> {

    @Override
    public Optional<User> findById(int id) throws DAOException {
        String sql = "SELECT * FROM Users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapResultSetToUser(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DAOException("Error finding user by ID", e);
        }
    }

    @Override
    public List<User> findAll() throws DAOException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users ORDER BY login";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            return users;
        } catch (SQLException e) {
            throw new DAOException("Error retrieving all users", e);
        }
    }


    public List<User> findLoggedInUsers() throws DAOException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE isActive = true";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            return users;
    
        } catch (SQLException e) {
            throw new DAOException("Error fetching online users", e);
        }
    }
    
    @Override
    public void save(User user) throws DAOException {
        String sql = "INSERT INTO Users (login, password) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setUserParameters(stmt, user);
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error creating user", e);
        }
    }

    @Override
    public void update(User user) throws DAOException {
        String sql = "UPDATE Users SET login = ?, password = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setUserParameters(stmt, user);
            stmt.setInt(3, user.getId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("User not found with ID: " + user.getId());
            }
        } catch (SQLException e) {
            throw new DAOException("Error updating user", e);
        }
    }

    @Override
    public void delete(int id) throws DAOException {
        executeTransaction(conn -> {
            // Delete user's tasks first
            String deleteTasksSql = "DELETE FROM Tasks WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteTasksSql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

            // Delete user's group memberships
            String deleteGroupMembershipsSql = "DELETE FROM GroupMembers WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteGroupMembershipsSql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

            // Finally delete the user
            String deleteUserSql = "DELETE FROM Users WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteUserSql)) {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("User not found with ID: " + id);
                }
            }
        });
    }

    public Optional<User> findByLogin(String login) throws DAOException {
        String sql = "SELECT * FROM Users WHERE login = ?";
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    User user = mapResultSetToUser(rs);
                    user.setIsActive(true);
                    updateIsActiveDB(user);
                    System.out.println("User successfully updated");
                    return Optional.of(user);
                }
                else{
                    return Optional.empty();

                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error finding user by login", e);
        }
    }

    private void updateIsActiveDB(User user) throws DAOException{
        String sql = "UPDATE Users SET isActive = ? WHERE id = ?";
        try (Connection conn = getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1, user.getIsActive());
            stmt.setInt(2, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e){
            throw new DAOException("Error updating status", e);
        }
    }




    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("password"));
        return user;
    }

    private void setUserParameters(PreparedStatement stmt, User user) throws SQLException {
        stmt.setString(1, user.getLogin());
        stmt.setString(2, user.getPassword());
    }
}