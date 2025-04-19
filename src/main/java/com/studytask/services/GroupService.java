package com.studytask.services;

import com.studytask.dao.GenericDAO;
import com.studytask.dao.GroupDAO;
import com.studytask.models.Group;
import com.studytask.exceptions.ServiceException;
import com.studytask.exceptions.DAOException;
import java.util.List;
import java.util.Optional;

public class GroupService {
    private final GroupDAO groupDAO;

    public GroupService(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    public GenericDAO<Group> getDao() {
        return this.groupDAO;
    }
    
    public List<Group> getAllGroups() throws ServiceException {
        try {
            return groupDAO.findAll();
        } catch (DAOException e) {
            throw new ServiceException("Failed to retrieve groups", e);
        }
    }

    public Optional<Group> getGroup(int id) throws ServiceException {
        try {
            return groupDAO.findById(id);
        } catch (DAOException e) {
            throw new ServiceException("Failed to retrieve group", e);
        }
    }

    public void createGroup(Group group) throws ServiceException {
        try {
            groupDAO.save(group);
        } catch (DAOException e) {
            throw new ServiceException("Failed to create group", e);
        }
    }

    public void updateGroup(Group group) throws ServiceException {
        try {
            groupDAO.update(group);
        } catch (DAOException e) {
            throw new ServiceException("Failed to update group", e);
        }
    }

    public void deleteGroup(int id) throws ServiceException {
        try {
            groupDAO.delete(id);
        } catch (DAOException e) {
            throw new ServiceException("Failed to delete group", e);
        }
    }

    public List<Group> getUserGroups(int userId) throws ServiceException {
        try {
            return groupDAO.findByUserId(userId);
        } catch (DAOException e) {
            throw new ServiceException("Failed to retrieve user's groups", e);
        }
    }

    public void addUserToGroup(int userId, int groupId) throws ServiceException {
        try {
            groupDAO.addMember(groupId, userId); // Order of parameters match DAO
        } catch (DAOException e) {
            throw new ServiceException("Failed to add user to group", e);
        }
    }

    public void removeUserFromGroup(int userId, int groupId) throws ServiceException {
        try {
            groupDAO.removeMember(groupId, userId); // Order of parameters match DAO
        } catch (DAOException e) {
            throw new ServiceException("Failed to remove user from group", e);
        }
    }
}