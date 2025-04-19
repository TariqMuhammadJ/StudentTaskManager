package com.studytask.controllers;

import java.io.IOException;
import java.sql.Date;
import java.util.Optional;

import com.studytask.exceptions.ServiceException;
import com.studytask.models.Task;
import com.studytask.services.ServiceFactory;
import com.studytask.services.TaskService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/tasks")
public class TaskServlet extends HttpServlet {
    private final TaskService taskService = ServiceFactory.getTaskService();
    private static final int DUMMY_USER_ID = 1;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        System.out.println("TaskServlet: doGet started");
        try {
            request.setAttribute("tasks", taskService.getUserTasks(DUMMY_USER_ID));
            request.getRequestDispatcher("/tasks.jsp").forward(request, response);
        } catch (ServiceException e) {
            System.out.println("TaskServlet: Error fetching tasks: " + e.getMessage());
            request.setAttribute("error", "Failed to load tasks: " + e.getMessage());
            request.getRequestDispatcher("/tasks.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        System.out.println("TaskServlet: doPost started");
        String action = request.getParameter("action");
        System.out.println("TaskServlet: action = " + action);
        
        try {
            switch (action) {
                case "create":
                    System.out.println("TaskServlet: Creating new task");
                    createTask(request, response);
                    break;
                case "update":
                    System.out.println("TaskServlet: Updating task");
                    updateTask(request, response);
                    break;
                case "delete":
                    System.out.println("TaskServlet: Deleting task");
                    deleteTask(request, response);
                    break;
                case "complete":
                    System.out.println("TaskServlet: Completing task");
                    completeTask(request, response);
                    break;
                default:
                    System.out.println("TaskServlet: Invalid action, redirecting to tasks");
                    response.sendRedirect(request.getContextPath() + "/tasks");
            }
        } catch (ServiceException e) {
            System.out.println("TaskServlet: Error processing task: " + e.getMessage());
            request.setAttribute("error", "Task operation failed: " + e.getMessage());
            request.getRequestDispatcher("/tasks.jsp").forward(request, response);
        }
    }

    private void createTask(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, ServiceException {
        System.out.println("TaskServlet: createTask started");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String dueDateStr = request.getParameter("due_date");
        
        System.out.println("Task details:");
        System.out.println("Title: " + title);
        System.out.println("Description: " + description);
        System.out.println("Due Date: " + dueDateStr);

        try {
            Date dueDate = Date.valueOf(dueDateStr);
            Task task = new Task(0, title, description, dueDate, false, DUMMY_USER_ID);
            taskService.createTask(task);
            System.out.println("TaskServlet: Task created successfully");
            response.sendRedirect(request.getContextPath() + "/listTasks");
        } catch (IllegalArgumentException e) {
            System.out.println("TaskServlet: Error creating task - Invalid date format: " + e.getMessage());
            throw new ServiceException("Invalid date format. Use YYYY-MM-DD");
        }
    }

    private void updateTask(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, ServiceException {
        int taskId = Integer.parseInt(request.getParameter("taskId"));
        Optional<Task> existingTask = taskService.getTask(taskId);
        
        if (existingTask.isPresent()) {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String dueDateStr = request.getParameter("due_date");
            
            Date dueDate = null;
            if (dueDateStr != null && !dueDateStr.isEmpty()) {
                dueDate = Date.valueOf(dueDateStr);
            }

            Task updatedTask = new Task(
                taskId,
                title,
                description,
                dueDate,
                existingTask.get().isCompleted(),
                DUMMY_USER_ID
            );
            
            taskService.updateTask(updatedTask);
            System.out.println("TaskServlet: Task updated successfully");
        }
        response.sendRedirect(request.getContextPath() + "/listTasks");
    }

    private void deleteTask(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, ServiceException {
        int taskId = Integer.parseInt(request.getParameter("taskId"));
        taskService.deleteTask(taskId);
        System.out.println("TaskServlet: Task deleted successfully");
        response.sendRedirect(request.getContextPath() + "/listTasks");
    }

    private void completeTask(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, ServiceException {
        int taskId = Integer.parseInt(request.getParameter("taskId"));
        Optional<Task> existingTask = taskService.getTask(taskId);
        
        if (existingTask.isPresent()) {
            Task task = existingTask.get();
            task.setCompleted(true);
            taskService.updateTask(task);
            System.out.println("TaskServlet: Task marked as completed");
        }
        response.sendRedirect(request.getContextPath() + "/listTasks");
    }
}