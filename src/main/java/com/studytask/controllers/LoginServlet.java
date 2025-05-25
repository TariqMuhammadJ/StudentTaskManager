package com.studytask.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import com.studytask.models.User;
import com.studytask.services.ServiceFactory;
import com.studytask.services.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@SuppressWarnings("serial")
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserService userService = ServiceFactory.getUserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Forward to login page
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Check if a valid user session already exists
            HttpSession session = request.getSession(false);
            if (session != null) {
                @SuppressWarnings("unchecked")
                Optional<User> existingUser = (Optional<User>) session.getAttribute("user");
                if (existingUser != null && existingUser.isPresent()) {
                    // User already logged in
                    response.sendRedirect("tasks.jsp");
                    return;
                }
            }

            // Handle login
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            Optional<User> user = userService.login(username, password);
            if (user.isPresent()) {
                // Create new session
                HttpSession newSession = request.getSession(true);
                newSession.setAttribute("user", user);
                newSession.setAttribute("logintime", LocalDateTime.now());
                newSession.setMaxInactiveInterval(30*60); // 30 minutes

                System.out.println("Logged in: " + username);
                response.sendRedirect("tasks.jsp");
            } else {
                request.setAttribute("Error", "Invalid username or password");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            request.setAttribute("error", "Login failed.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

}