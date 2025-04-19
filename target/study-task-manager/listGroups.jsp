<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    // Redirect to servlet if accessed directly
    if (request.getAttribute("groups") == null) {
        response.sendRedirect(request.getContextPath() + "/listGroups");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Group List</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 600px;
            margin: 50px auto;
            padding: 20px;
            background: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        h1 {
            text-align: center;
            color: #333;
        }
        .group-list {
            margin: 20px 0;
        }
        .group-item {
            padding: 10px;
            background: #f8f8f8;
            margin-bottom: 10px;
            border-left: 5px solid #007BFF;
        }
        .no-groups {
            text-align: center;
            color: #666;
            padding: 20px;
        }
        .back-button {
            display: block;
            width: 100px;
            margin: 20px auto;
            padding: 10px;
            background-color: #007BFF;
            color: white;
            text-align: center;
            text-decoration: none;
            border: none;
            cursor: pointer;
        }
        .back-button:hover {
            background-color: #0056b3;
        }
        .datetime-info {
            text-align: center;
            color: #666;
            margin-bottom: 20px;
            font-size: 0.9em;
        }
        .navigation {
            text-align: center;
            margin-top: 20px;
        }
        .navigation a {
            color: #007BFF;
            text-decoration: none;
            margin: 0 10px;
        }
        .navigation a:hover {
            text-decoration: underline;
        }
        .group-actions {
            margin-top: 10px;
        }
        .action-button {
            padding: 5px 10px;
            margin-right: 5px;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            color: white;
        }
        .edit-button {
            background-color: #28a745;
        }
        .edit-button:hover {
            background-color: #218838;
        }
        .delete-button {
            background-color: #dc3545;
        }
        .delete-button:hover {
            background-color: #c82333;
        }
        input[type="text"] {
            padding: 5px;
            border: 1px solid #ddd;
            border-radius: 3px;
            margin-right: 5px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            color: #666;
        }
        .edit-form {
            padding: 15px;
            background: #fff;
            border-radius: 5px;
        }
    </style>
</head>
<body>
    <%@ page import="java.time.ZonedDateTime, java.time.format.DateTimeFormatter" %>
    <% 
        ZonedDateTime now = ZonedDateTime.now();
        String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    %>

    <div class="container">
        <div class="datetime-info">
            <p>Current Date and Time (UTC): <%= formattedDate %></p>
        </div>

        <h1>Group List</h1>
        
        <div class="group-list">
            <c:choose>
                <c:when test="${empty groups}">
                    <div class="no-groups">
                        <p>No groups available</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="group" items="${groups}">
                        <div class="group-item">
                            <c:choose>
                                <c:when test="${group.id == editGroupId}">
                                    <div class="edit-form">
                                        <form action="groups" method="post">
                                            <input type="hidden" name="action" value="update">
                                            <input type="hidden" name="groupId" value="${group.id}">
                                            
                                            <div class="form-group">
                                                <label for="newName">Group Name:</label>
                                                <input type="text" id="newName" name="newName" 
                                                       value="${group.name}" style="width: 100%;" required>
                                            </div>
                                            
                                            <div class="form-group">
                                                <label for="newMembers">Members (comma-separated):</label>
                                                <input type="text" id="newMembers" name="newMembers" 
                                                       value="<c:forEach var='member' items='${group.members}' varStatus='status'>${member.login}${!status.last ? ', ' : ''}</c:forEach>"
                                                       style="width: 100%;" required>
                                            </div>
                                            
                                            <div class="group-actions">
                                                <button type="submit" class="action-button edit-button">Save Changes</button>
                                                <a href="listGroups" class="action-button delete-button" 
                                                   style="text-decoration: none;">Cancel</a>
                                            </div>
                                        </form>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <h3>${group.name}</h3>
                                    <p>Members: 
                                        <c:forEach var="member" items="${group.members}" varStatus="status">
                                            ${member.login}${!status.last ? ', ' : ''}
                                        </c:forEach>
                                    </p>
                                    <div class="group-actions">
                                        <form action="groups" method="post" style="display: inline;">
                                            <input type="hidden" name="action" value="edit">
                                            <input type="hidden" name="groupId" value="${group.id}">
                                            <button type="submit" class="action-button edit-button">Modify</button>
                                        </form>
                                        <form action="groups" method="post" style="display: inline;">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="groupId" value="${group.id}">
                                            <button type="submit" class="action-button delete-button" 
                                                    onclick="return confirm('Are you sure you want to delete this group?');">Delete</button>
                                        </form>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>

        <a href="groups.jsp" class="back-button">Go Back</a>

        <div class="navigation">
            <a href="tasks.jsp">Tasks</a> |
            <a href="profile.jsp">Profile</a> |
            <a href="index.jsp">Logout</a>
        </div>
    </div>
</body>
</html>