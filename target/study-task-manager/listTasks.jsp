<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Task List</title>
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
        .task-list {
            margin: 20px 0;
        }
        .task-item {
            padding: 10px;
            background: #f8f8f8;
            margin-bottom: 10px;
            border-left: 5px solid #007BFF;
        }
        .no-tasks {
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
            border-bottom: 1px solid #eee;
            padding-bottom: 10px;
        }
        .navigation {
            text-align: center;
            margin-top: 20px;
            padding-top: 10px;
            border-top: 1px solid #eee;
        }
        .navigation a {
            color: #007BFF;
            text-decoration: none;
            margin: 0 10px;
        }
        .navigation a:hover {
            text-decoration: underline;
        }
        .completed {
            color: #28a745;
        }
        .pending {
            color: #dc3545;
        }
        .task-actions {
            margin-top: 10px;
            text-align: right;
        }
        .action-button {
            padding: 5px 10px;
            margin-left: 5px;
            background-color: #007BFF;
            color: white;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }
        .delete-button {
            background-color: #dc3545;
        }
        .complete-button {
            background-color: #28a745;
        }
        .modify-button {
            background-color: #ffc107;
        }
        .task-item form div {
            margin-bottom: 10px;
        }
        .task-item form input[type="text"],
        .task-item form input[type="date"] {
            width: 100%;
            padding: 5px;
            border: 1px solid #ddd;
            border-radius: 3px;
        }
        .task-item form label {
            display: block;
            margin-bottom: 5px;
            color: #666;
        }
    </style>
</head>
<body>
    <div class="container">
        <%@ page import="java.time.ZonedDateTime, java.time.format.DateTimeFormatter" %>
        <% 
            ZonedDateTime now = ZonedDateTime.now();
            String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        %>
        <div class="datetime-info">
            <p>Current Date and Time (UTC): <%= formattedDate %></p>
        </div>

        <h1>Task List</h1>
        
        <div class="task-list">
            <c:choose>
                <c:when test="${empty tasks}">
                    <div class="no-tasks">
                        <p>No tasks available</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="task" items="${tasks}">
                        <div class="task-item">
                            <c:choose>
                                <c:when test="${param.editTaskId eq task.id}">
                                    <!-- Edit form -->
                                    <form action="tasks" method="post">
                                        <input type="hidden" name="action" value="update">
                                        <input type="hidden" name="taskId" value="${task.id}">
                                        <div>
                                            <label>Title:</label>
                                            <input type="text" name="title" value="${task.title}" required>
                                        </div>
                                        <div>
                                            <label>Description:</label>
                                            <input type="text" name="description" value="${task.description}">
                                        </div>
                                        <div>
                                            <label>Due Date:</label>
                                            <input type="date" name="due_date" value="${task.dueDate}" required>
                                        </div>
                                        <div class="task-actions">
                                            <button type="submit" class="action-button complete-button">Save</button>
                                            <a href="listTasks" class="action-button delete-button">Cancel</a>
                                        </div>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <!-- Display mode -->
                                    <h3>${task.title}</h3>
                                    <p>${task.description}</p>
                                    <p>Due Date: ${task.dueDate}</p>
                                    <p>Status: 
                                        <span class="${task.completed ? 'completed' : 'pending'}">
                                            ${task.completed ? 'Completed' : 'Pending'}
                                        </span>
                                    </p>
                                    <div class="task-actions">
                                        <form action="tasks" method="post" style="display: inline;">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="taskId" value="${task.id}">
                                            <button type="submit" class="action-button delete-button">Delete</button>
                                        </form>
                                        <c:if test="${!task.completed}">
                                            <form action="tasks" method="post" style="display: inline;">
                                                <input type="hidden" name="action" value="complete">
                                                <input type="hidden" name="taskId" value="${task.id}">
                                                <button type="submit" class="action-button complete-button">Complete</button>
                                            </form>
                                            <a href="listTasks?editTaskId=${task.id}" class="action-button modify-button">Modify</a>
                                        </c:if>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
        
        <a href="tasks.jsp" class="back-button">Back</a>

        <div class="navigation">
            <a href="groups.jsp">Groups</a> |
            <a href="profile.jsp">Profile</a> |
            <a href="index.jsp">Logout</a>
            <a href="onlineUsers.jsp">Online</a>
        </div>
    </div>
</body>
</html>