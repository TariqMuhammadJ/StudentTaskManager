package com.studytask.controllers;

import java.io.IOException;
import java.util.List;

import com.studytask.exceptions.ServiceException;
import com.studytask.models.Task;
import com.studytask.services.ServiceFactory;
import com.studytask.services.TaskService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/listTasks")
public class ListTasksServlet extends HttpServlet {
    private final TaskService taskService = ServiceFactory.getTaskService();
    private static final int DUMMY_USER_ID = 1;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        System.out.println("ListTasksServlet: Fetching tasks");
        
        try {
            List<Task> tasks = taskService.getUserTasks(DUMMY_USER_ID);
            System.out.println("ListTasksServlet: Found " + tasks.size() + " tasks");
            request.setAttribute("tasks", tasks);
            request.getRequestDispatcher("/listTasks.jsp").forward(request, response);
        } catch (ServiceException e) {
            System.out.println("ListTasksServlet: Error fetching tasks: " + e.getMessage());
            request.setAttribute("error", "Failed to load tasks: " + e.getMessage());
            request.getRequestDispatcher("/listTasks.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}