package com.studytask.controllers;

import java.io.IOException;

import com.studytask.exceptions.ServiceException;
import com.studytask.services.ServiceFactory;
import com.studytask.services.TaskService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/deleteTask")
public class DeleteTaskServlet extends HttpServlet {
    private final TaskService taskService = ServiceFactory.getTaskService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect("login");
            return;
        }

        String taskId = request.getParameter("taskId");
        if (taskId != null && !taskId.isEmpty()) {
            try {
                taskService.deleteTask(Integer.parseInt(taskId));
                response.sendRedirect("tasks");
            } catch (ServiceException e) {
                request.setAttribute("error", "Failed to delete task: " + e.getMessage());
                request.getRequestDispatcher("/views/tasks.jsp").forward(request, response);
            }
        }
    }
}