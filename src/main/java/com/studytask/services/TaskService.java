package com.studytask.services;

import com.studytask.dao.TaskDAO;
import com.studytask.models.Task;
import com.studytask.observers.TaskObserver;
import com.studytask.exceptions.ServiceException;
import com.studytask.exceptions.DAOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskService {
    private final TaskDAO taskDAO;
    private final List<TaskObserver> observers;

    public TaskService(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
        this.observers = new ArrayList<>();
    }

    public void addObserver(TaskObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TaskObserver observer) {
        observers.remove(observer);
    }

    public List<Task> getAllTasks() throws ServiceException {
        try {
            return taskDAO.findAll();
        } catch (DAOException e) {
            throw new ServiceException("Failed to retrieve tasks", e);
        }
    }

    public List<Task> getUserTasks(int userId) throws ServiceException {
        try {
            return taskDAO.findByUserId(userId);
        } catch (DAOException e) {
            throw new ServiceException("Failed to retrieve user tasks", e);
        }
    }

    public Optional<Task> getTask(int id) throws ServiceException {
        try {
            return taskDAO.findById(id);
        } catch (DAOException e) {
            throw new ServiceException("Failed to retrieve task", e);
        }
    }

    public void createTask(Task task) throws ServiceException {
        try {
            taskDAO.save(task);
            notifyTaskCreated(task);
        } catch (DAOException e) {
            throw new ServiceException("Failed to create task", e);
        }
    }

    public void updateTask(Task task) throws ServiceException {
        try {
            taskDAO.update(task);
            notifyTaskUpdated(task);
        } catch (DAOException e) {
            throw new ServiceException("Failed to update task", e);
        }
    }

    public void deleteTask(int id) throws ServiceException {
        try {
            taskDAO.delete(id);
            notifyTaskDeleted(id);
        } catch (DAOException e) {
            throw new ServiceException("Failed to delete task", e);
        }
    }

    public void completeTask(int taskId) throws ServiceException {
        try {
            Optional<Task> optionalTask = taskDAO.findById(taskId);
            if (optionalTask.isPresent()) {
                Task task = optionalTask.get();
                task.setCompleted(true);
                taskDAO.update(task);
                notifyTaskCompleted(task);
            } else {
                throw new ServiceException("Task not found with ID: " + taskId);
            }
        } catch (DAOException e) {
            throw new ServiceException("Failed to complete task", e);
        }
    }

    // Private methods for observer notifications
    private void notifyTaskCreated(Task task) {
        for (TaskObserver observer : observers) {
            observer.onTaskCreated(task);
        }
    }

    private void notifyTaskUpdated(Task task) {
        for (TaskObserver observer : observers) {
            observer.onTaskUpdated(task);
        }
    }

    private void notifyTaskCompleted(Task task) {
        for (TaskObserver observer : observers) {
            observer.onTaskCompleted(task);
        }
    }

    private void notifyTaskDeleted(int taskId) {
        for (TaskObserver observer : observers) {
            observer.onTaskDeleted(taskId);
        }
    }
}