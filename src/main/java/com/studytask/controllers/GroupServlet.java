package com.studytask.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.studytask.exceptions.ServiceException;
import com.studytask.models.Group;
import com.studytask.models.User;
import com.studytask.services.GroupService;
import com.studytask.services.ServiceFactory;
import com.studytask.services.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/groups")
public class GroupServlet extends HttpServlet {
    private final GroupService groupService = ServiceFactory.getGroupService();
    private final UserService userService = ServiceFactory.getUserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action != null) {
            switch (action) {
                case "delete":
                    handleDeleteGroup(request, response);
                    break;
                case "edit":
                    handleEditGroup(request, response);
                    break;
                case "update":
                    handleUpdateGroup(request, response);
                    break;
                default:
                    handleCreateGroup(request, response);
                    break;
            }
        } else {
            handleCreateGroup(request, response);
        }
    }

    private void handleEditGroup(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int groupId = Integer.parseInt(request.getParameter("groupId"));
            request.setAttribute("editGroupId", groupId);
            List<Group> groups = groupService.getAllGroups();
            request.setAttribute("groups", groups);
            request.getRequestDispatcher("/listGroups.jsp").forward(request, response);
        } catch (ServiceException | NumberFormatException e) {
            System.err.println("Failed to prepare group edit: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/listGroups");
        }
    }

    private void handleUpdateGroup(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int groupId = Integer.parseInt(request.getParameter("groupId"));
            String newName = request.getParameter("newName");
            String newMembers = request.getParameter("newMembers");
            
            if (newName != null && !newName.trim().isEmpty()) {
                Group group = groupService.getGroup(groupId)
                    .orElseThrow(() -> new ServiceException("Group not found"));
                group.setName(newName.trim());
                
                // Handle members update
                if (newMembers != null && !newMembers.trim().isEmpty()) {
                    List<User> updatedMembers = new ArrayList<>();
                    String[] memberLogins = newMembers.split(",");
                    for (String memberLogin : memberLogins) {
                        String trimmedLogin = memberLogin.trim();
                        if (!trimmedLogin.isEmpty()) {
                            try {
                                User member = ensureUserExists(trimmedLogin, "defaultPassword");
                                updatedMembers.add(member);
                            } catch (ServiceException e) {
                                System.err.println("Failed to add member " + trimmedLogin + ": " + e.getMessage());
                            }
                        }
                    }
                    group.setMembers(updatedMembers);
                }
                
                groupService.updateGroup(group);
                System.out.println("Group updated successfully: " + groupId);
            }
            
            response.sendRedirect(request.getContextPath() + "/listGroups");
        } catch (ServiceException | NumberFormatException e) {
            System.err.println("Failed to update group: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/listGroups");
        }
    }

    private void handleDeleteGroup(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int groupId = Integer.parseInt(request.getParameter("groupId"));
            groupService.deleteGroup(groupId);
            System.out.println("Group deleted successfully: " + groupId);
            response.sendRedirect(request.getContextPath() + "/listGroups");
        } catch (ServiceException | NumberFormatException e) {
            System.err.println("Failed to delete group: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/listGroups");
        }
    }

    private void handleCreateGroup(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        System.out.println("\n========== GROUP CREATION START ==========");
        
        String name = request.getParameter("name");
        String membersStr = request.getParameter("members");
        
        System.out.println("Attempting to create group:");
        System.out.println("Group Name: " + name);
        System.out.println("Members: " + membersStr);
        
        try {
            // Get the current user from session
            HttpSession session = request.getSession(false);         
            Group newGroup = new Group(name.trim());
            groupService.createGroup(newGroup);
            
            // this needs a-lot of working - over here
            // add the members and ensure the group exists - with the correct login member
            // notify the members that you have been added to a group
            
            // Add only the specified members
            if (membersStr != null && !membersStr.trim().isEmpty()) {
                String[] memberLogins = membersStr.split(",");
                for (String memberLogin : memberLogins) {
                    String trimmedLogin = memberLogin.trim();
                    if (!trimmedLogin.isEmpty()) {
                        try {
                            User member = ensureUserExists(trimmedLogin, "defaultPassword");
                            groupService.addUserToGroup(member.getId(), newGroup.getId());
                        } catch (ServiceException e) {
                            System.err.println("Failed to add member " + trimmedLogin + ": " + e.getMessage());
                        }
                    }
                }
            }
            
            System.out.println("Group creation completed successfully!");
            
        } catch (ServiceException e) {
            System.err.println("ERROR during group creation:");
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("========== GROUP CREATION END ==========\n");
        response.sendRedirect(request.getContextPath() + "/listGroups");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        System.out.println("\n========== LOADING GROUPS PAGE ==========");
        try {
            List<Group> groups = groupService.getAllGroups();
            System.out.println("Retrieved " + groups.size() + " groups");
            request.setAttribute("groups", groups);
            
            // Using session user
            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");
            request.setAttribute("user", currentUser);
            
            request.getRequestDispatcher("/groups.jsp").forward(request, response);
            System.out.println("========== GROUPS PAGE LOADED ==========\n");
        } catch (ServiceException e) {
            System.err.println("ERROR loading groups:");
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Failed to load groups: " + e.getMessage());
            request.getRequestDispatcher("/groups.jsp").forward(request, response);
        }
    }

    private User ensureUserExists(String login, String password) throws ServiceException {
        try {
            User user = new User(login, password);
            userService.register(user);
            System.out.println("Created new user: " + login);
            return user;
        } catch (ServiceException e) {
            return userService.login(login, password)
                .orElseThrow(() -> new ServiceException("Cannot create or find user: " + login));
        }
    }
}