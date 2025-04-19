package com.studytask.services;

import com.studytask.dao.UserDAO;
import com.studytask.dao.TaskDAO;
import com.studytask.dao.GroupDAO;

public class ServiceFactory {
    // Create DAO instances
    private static final UserDAO userDAO = new UserDAO();
    private static final TaskDAO taskDAO = new TaskDAO();
    private static final GroupDAO groupDAO = new GroupDAO();

    // Create Service instances with their required DAOs
    private static final UserService userService = new UserService(userDAO);
    private static final TaskService taskService = new TaskService(taskDAO);
    private static final GroupService groupService = new GroupService(groupDAO);

    // Private constructor to prevent instantiation
    private ServiceFactory() {
        // Prevents external instantiation
    }

    // Static getter methods for services
    public static UserService getUserService() {
        return userService;
    }

    public static TaskService getTaskService() {
        return taskService;
    }

    public static GroupService getGroupService() {
        return groupService;
    }
}