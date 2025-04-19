package com.studytask.controllers;

import java.io.IOException;

import com.studytask.exceptions.ServiceException;
import com.studytask.models.User;
import com.studytask.services.ServiceFactory;
import com.studytask.services.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserService userService = ServiceFactory.getUserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            response.sendRedirect("tasks");
            return;
        }
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get parameters from request
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        if (login == null || login.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Login and password are required");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        try {
            User newUser = new User();
            newUser.setLogin(login);
            newUser.setPassword(password);

            userService.register(newUser);
            
            // Redirect to login page after successful registration
            response.sendRedirect("login");
            
        } catch (ServiceException e) {
            System.err.println("Registration ERROR"+ e.getMessage());
            request.setAttribute("error", "Registration failed: " + e.getMessage());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}