package com.studytask.controllers;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

@WebServlet("/onlineUsers")
public class OnlineUsersServlet extends HttpServlet {
    private final UserService userService;

    // Constructor to initialize userService using a ServiceFactory
    public OnlineUsersServlet() {
        this.userService = ServiceFactory.getUserService(); // Assuming this is how your factory works
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
        	HttpSession session = request.getSession(false);
        	Optional<User> user = (Optional<User>) session.getAttribute("user");
        	if (session != null && user.isPresent()) {
        		List<User> getOnlineUsers = userService.getOnlineUsers(user.get().getId());
        		request.setAttribute("onlineUsers", getOnlineUsers); // use camelCase attribute
                System.out.println(getOnlineUsers);
                request.getRequestDispatcher("onlineUsers.jsp").forward(request, response);
        		
        	} // Make sure this method exists
        	else {
        		String message = "There are no online Users Currently";
        		request.setAttribute("message", message);
        		request.getRequestDispatcher("onlineUsers.jsp").forward(request, response);
        		
        	}
        } catch (ServiceException e) {
            System.err.println("Error retrieving online users: " + e.getMessage());
            request.setAttribute("error", "Failed to load online users: " + e.getMessage());
            request.getRequestDispatcher("onlineUsers.jsp").forward(request, response);
        }
    }
}
