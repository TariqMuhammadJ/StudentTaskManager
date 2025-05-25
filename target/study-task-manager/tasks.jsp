<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <title>Tasks - StudyTask</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 800px;
            margin: 50px auto;
            padding: 20px;
            background: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .header{
        	display:flex;
        	justify-content:space-between;
        	align-items:center;
        }
        
        .notifications{
        
        		margin-bottom: 20px;
            	padding-bottom: 10px;
        }
        
        .notifications > i{
        font-size:2vw;
        margin: 0 2vw 0 2vw;
        color:green;
        
        
        }
        
        .notifications > ul{
        	display:none;	
        
        }
        
        h1, h2 {
            color: #333;
            text-align: center;
        }
        .task-form {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            color: #666;
        }
        input[type="text"],
        input[type="date"] {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .btn {
            padding: 10px 20px;
            background-color: #007BFF;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            margin-right: 10px;
        }
        .btn:hover {
            background-color: #0056b3;
        }
        .task-list {
            list-style: none;
            padding: 0;
        }
        .task-item {
            background: #f8f9fa;
            padding: 15px;
            margin-bottom: 10px;
            border-left: 4px solid #007BFF;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .task-info {
            flex-grow: 1;
        }
        .task-actions {
            margin-left: 20px;
        }
        .nav-links {
            text-align: center;
            margin-top: 30px;
            padding-top: 20px;
            border-top: 1px solid #ddd;
        }
        .nav-links a {
            color: #007BFF;
            text-decoration: none;
            margin: 0 15px;
            padding: 5px 10px;
        }
        .nav-links a:hover {
            text-decoration: underline;
        }
        .welcome-message {
            text-align: center;
            color: #666;
            margin-bottom: 20px;
        }
        .user-info {
            text-align: center;
            color: #666;
            margin-bottom: 20px;
            font-size: 0.9em;
            border-bottom: 1px solid #eee;
            padding-bottom: 10px;
        }
        .button-group {
            display: flex;
            gap: 10px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
        <div class="user-info">
            <%@ page import="java.time.ZonedDateTime, java.time.format.DateTimeFormatter" %>
            <% 
                ZonedDateTime now = ZonedDateTime.now();
                String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            %>
            <p>Current Date and Time (UTC): <%= formattedDate %></p>
        </div>
        <div class="notifications">
        	<i class="fas fa-bell"></i>
        	<ul>
        		<li> </li>
        	
        	</ul>  
        </div>
        
        </div>
        <div class="task-form">
            <h2>Add New Task</h2>
            <form action="tasks" method="post">
                <input type="hidden" name="action" value="create">
                <div class="form-group">
                    <label for="title">Title:</label>
                    <input type="text" id="title" name="title" required>
                </div>
                <div class="form-group">
                    <label for="description">Description:</label>
                    <input type="text" id="description" name="description">
                </div>
                <div class="form-group">
                    <label for="due_date">Due Date:</label>
                    <input type="date" 
                           id="due_date" 
                           name="due_date" 
                           required
                           min="2024-01-01" 
                           max="2030-12-31">
                </div>
                <div class="button-group">
                    <button type="submit" class="btn">Add Task</button>
                    <a href="listTasks" class="btn">View Tasks</a>
                </div>
            </form>
        </div>

        <div class="task-list-container">
            <h2>Your Tasks</h2>
            <ul class="task-list">
                <c:forEach var="task" items="${tasks}">
                    <li class="task-item">
                        <div class="task-info">
                            <strong>${task.title}</strong>
                            <p>${task.description}</p>
                            <small>Due: ${task.dueDate}</small>
                        </div>
                        <div class="task-actions">
                            <form action="tasks" method="post" style="display: inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="taskId" value="${task.id}">
                                <button type="submit" class="btn">Delete</button>
                            </form>
                            <form action="tasks" method="post" style="display: inline;">
                                <input type="hidden" name="action" value="complete">
                                <input type="hidden" name="taskId" value="${task.id}">
                                <button type="submit" class="btn">Complete</button>
                            </form>
                        </div>
                    </li>
                </c:forEach>
            </ul>
        </div>

        <div class="nav-links">
            <a href="profile.jsp">Profile</a>
            <a href="groups.jsp">Groups</a>
            <a href="#" onClick="document.getElementById('logoutform').submit(); return false;">Logout</a>
            <form id="logoutform" action="logout" method="post" style="display:none;"></form>
            <a href="onlineUsers">Online</a>
        </div>
    </div>
</body>
</html>