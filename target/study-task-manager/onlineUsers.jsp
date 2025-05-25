<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.ZonedDateTime, java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.Optional" %>
<%@ page import="com.studytask.models.User" %>
<!DOCTYPE html>
<html>
<head>
    <title>Online Users</title>
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
        .user-list {
            margin: 20px 0;
        }
        .user-item {
            padding: 10px;
            background: #f8f8f8;
            margin-bottom: 10px;
            border-left: 5px solid #28a745;
        }
        .no-users {
            text-align: center;
            color: #666;
            padding: 20px;
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
    </style>
</head>
<body>
<%
    ZonedDateTime now = ZonedDateTime.now();
    String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    //int UserID = (int) session.getAttribute("userId");
    Optional<User> userOpt = (Optional<User>) session.getAttribute("user");
    int userID = -1;
    if (userOpt.isPresent()) {
        User user = userOpt.get();
        System.out.println("Username: " + user.getLogin());
        System.out.println("UserID: " + user.getId());// Or whatever fields you have
        userID = user.getId();
    }
    

%>
<div class="container">
    <div class="datetime-info">
        <p>Current Date and Time (UTC): <%= formattedDate %></p>
    </div>

    <h1>Online Users</h1>

    <div class="user-list" id="user-parent" data-parent-id="<%=userID%>">
        <c:choose>
            <c:when test="${empty onlineUsers}">
                <div class="no-users">
                    <p>No users are currently online.</p>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach var="user" items="${onlineUsers}">
                    <div class="user-item">
                        <h3>
                            ${user.login}
                            <a data-user-id="${user.id}" href="chat.jsp?userId=<%=userID%>&targetid=${user.id}" title="Chat with ${user.login}" class="chat-icon">💬</a>
                        </h3>
                        <p>User ID: ${user.id}</p>
                        <p>Status: <span style="color: green;">Online</span></p>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>

    <a href="tasks.jsp" class="back-button">Back</a>

    <div class="navigation">
        <a href="groups.jsp">Groups</a> |
        <a href="profile.jsp">Profile</a> |
        <a href="#" onClick="document.getElementById('logoutform').submit(); return false;">Logout</a>
        <form id="logoutform" action="logout" method="post" style="display:none;"></form>
    </div>
</div>
<script type="module" src="./JS/chat.js"></script>
</body>
</html>
