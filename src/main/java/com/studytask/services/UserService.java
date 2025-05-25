package com.studytask.services;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.studytask.dao.UserDAO;
import com.studytask.exceptions.DAOException;
import com.studytask.exceptions.ServiceException;
import com.studytask.models.User;
import com.studytask.util.ConnectionPool;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Optional<User> login(String login, String password) throws ServiceException {
        try {
            Optional<User> userOpt = userDAO.findByLogin(login);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (user.getPassword().equals(password)) {
                    user.setIsActive(true);
                    updateIsActive(userOpt);
                    return Optional.of(user);
                }
            }
            return Optional.empty();
        } catch (DAOException e) {
            throw new ServiceException("Login failed", e);
        }
    }
    
    public void updateIsActive(Optional<User> userOpt) throws DAOException{
    	if(userOpt.isEmpty()) {
    		throw new DAOException("User not found : Cannot update status");
    	}
    	User user = userOpt.get();
        String sql = "UPDATE Users SET isActive = ? WHERE id = ?";
        try (Connection conn = ConnectionPool.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1, user.getIsActive());
            stmt.setInt(2, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e){
            throw new DAOException("Error updating status", e);
        }
    }


    public void register(User user) throws ServiceException {
        try {
            if (userDAO.findByLogin(user.getLogin()).isPresent()) {
                throw new ServiceException("User with this login already exists");
            }
            userDAO.save(user);
        } catch (DAOException e) {
            throw new ServiceException("Registration failed", e);
        }
    }

    public List<User> getAllUsers() throws ServiceException {
        try {
            return userDAO.findAll();
        } catch (DAOException e) {
            throw new ServiceException("Failed to retrieve users", e);
        }
    }
    

    public List<User> getOnlineUsers(int UserId) throws ServiceException{
        try{
            return userDAO.findLoggedInUsers(UserId);
        } catch(DAOException e){
            throw new ServiceException("Failed to retrieve Online Users", e);
        }
    }

    public Optional<User> getUser(int id) throws ServiceException {
        try {
            return userDAO.findById(id);
        } catch (DAOException e) {
            throw new ServiceException("Failed to retrieve user", e);
        }
    }

    public void updateUser(User user) throws ServiceException {
        try {
            userDAO.update(user);
        } catch (DAOException e) {
            throw new ServiceException("Failed to update user", e);
        }
    }

    public void deleteUser(int id) throws ServiceException {
        try {
            userDAO.delete(id);
        } catch (DAOException e) {
            throw new ServiceException("Failed to delete user", e);
        }
    }
}