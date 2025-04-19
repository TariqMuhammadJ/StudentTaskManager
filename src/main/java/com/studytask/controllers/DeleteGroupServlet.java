package com.studytask.controllers;

import java.io.IOException;

import com.studytask.exceptions.ServiceException;
import com.studytask.services.GroupService;
import com.studytask.services.ServiceFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@SuppressWarnings("serial")
@WebServlet("/deleteGroup")
public class DeleteGroupServlet extends HttpServlet {
    private final GroupService groupService = ServiceFactory.getGroupService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect("login");
            return;
        }

        String groupId = request.getParameter("groupId");
        if (groupId != null && !groupId.isEmpty()) {
            try {
                groupService.removeUserFromGroup(userId, Integer.parseInt(groupId));
                response.sendRedirect("groups");
            } catch (ServiceException e) {
                request.setAttribute("error", "Failed to leave group: " + e.getMessage());
                request.getRequestDispatcher("/views/groups.jsp").forward(request, response);
            }
        }
    }
}