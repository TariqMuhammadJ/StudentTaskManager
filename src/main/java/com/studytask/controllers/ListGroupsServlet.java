package com.studytask.controllers;

import java.io.IOException;
import java.util.List;

import com.studytask.exceptions.ServiceException;
import com.studytask.models.Group;
import com.studytask.services.GroupService;
import com.studytask.services.ServiceFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/listGroups")
public class ListGroupsServlet extends HttpServlet {
    private final GroupService groupService = ServiceFactory.getGroupService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Get all groups from the database
            List<Group> groups = groupService.getAllGroups();
            
            // Set them as an attribute to be displayed in the JSP
            request.setAttribute("groups", groups);
            
            // Forward to the listGroups.jsp
            request.getRequestDispatcher("/listGroups.jsp").forward(request, response);
            
        } catch (ServiceException e) {
            System.err.println("Error retrieving groups: " + e.getMessage());
            request.setAttribute("error", "Failed to load groups: " + e.getMessage());
            request.getRequestDispatcher("/listGroups.jsp").forward(request, response);
        }
    }
}