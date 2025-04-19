<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Group Management</title>
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
            border-radius: 5px;
        }
        h1, h2 {
            text-align: center;
            color: #333;
            margin-bottom: 20px;
        }
        form {
            display: flex;
            flex-direction: column;
            margin-bottom: 20px;
        }
        label {
            margin-bottom: 5px;
            color: #555;
        }
        input {
            padding: 8px;
            margin-bottom: 15px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
        }
        button {
            padding: 10px;
            background-color: #007BFF;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
        }
        button:hover {
            background-color: #0056b3;
        }
        ul {
            list-style-type: none;
            padding: 0;
        }
        li {
            padding: 15px;
            background: #f8f9fa;
            margin-bottom: 10px;
            border-radius: 4px;
            border-left: 5px solid #007BFF;
        }
        .group-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .group-info {
            flex-grow: 1;
        }
        .action-buttons {
            text-align: center;
            margin: 20px 0;
        }
        .view-groups-btn {
            display: inline-block;
            padding: 10px 20px;
            background-color: #28a745;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            margin: 10px 0;
        }
        .view-groups-btn:hover {
            background-color: #218838;
        }
        .delete-btn {
            background-color: #dc3545;
            color: white;
            border: none;
            padding: 8px 15px;
            cursor: pointer;
            border-radius: 4px;
            font-size: 14px;
            margin-left: 10px;
        }
        .delete-btn:hover {
            background-color: #c82333;
        }
        .links {
            text-align: center;
            margin-top: 20px;
            padding-top: 20px;
            border-top: 1px solid #ddd;
        }
        .links a {
            color: #007BFF;
            text-decoration: none;
            margin: 0 10px;
        }
        .links a:hover {
            text-decoration: underline;
        }
        hr {
            margin: 20px 0;
            border: 0;
            border-top: 1px solid #ddd;
        }
        .datetime-info {
            text-align: center;
            color: #666;
            margin-bottom: 10px;
            font-size: 0.9em;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Group Management</h1>
        
        <!-- DateTime Info -->
        <%@ page import="java.time.ZonedDateTime, java.time.format.DateTimeFormatter" %>
        <% 
            ZonedDateTime now = ZonedDateTime.now();
            String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        %>
        <div class="datetime-info">
            <p>Current Date and Time (UTC): <%= formattedDate %></p>
        </div>
        
        <!-- Create Group Form -->
        <form action="groups" method="post">
            <label for="groupName">Group Name:</label>
            <input type="text" id="groupName" name="name" required>
            
            <label for="members">Members (comma separated usernames):</label>
            <input type="text" id="members" name="members" required>
            
            <button type="submit">Create Group</button>
        </form>

        <!-- View Groups Button -->
        <div class="action-buttons">
            <a href="listGroups.jsp" class="view-groups-btn">View Existing Groups</a>
        </div>

        <hr>

        <!-- Display Groups -->
        <h2>Existing Groups</h2>
        <ul>
            <c:forEach var="group" items="${groups}">
                <li>
                    <div class="group-item">
                        <div class="group-info">
                            <strong>${group.name}</strong>
                            <div>
                                Members: 
                                <c:forEach var="member" items="${group.members}" varStatus="status">
                                    ${member.login}${!status.last ? ', ' : ''}
                                </c:forEach>
                            </div>
                        </div>
                        <form action="groups" method="post" style="display: inline; margin: 0;">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="groupId" value="${group.id}">
                            <button type="submit" class="delete-btn" 
                                    onclick="return confirm('Are you sure you want to delete this group?')">
                                Delete
                            </button>
                        </form>
                    </div>
                </li>
            </c:forEach>
        </ul>

        <div class="links">
            <a href="tasks.jsp">Tasks</a>
            <a href="profile.jsp">Profile</a>
            <a href="index.jsp">Logout</a>
        </div>
    </div>
</body>
</html>