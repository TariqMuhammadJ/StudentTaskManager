<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Welcome to StudyTask</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
            line-height: 1.6;
        }
        
        .container {
            max-width: 800px;
            margin: 50px auto;
            padding: 40px;
            background: #fff;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
            text-align: center;
        }
        
        h1 {
            color: #2c3e50;
            font-size: 2.5em;
            margin-bottom: 20px;
        }
        
        .description {
            color: #666;
            font-size: 1.1em;
            margin: 20px 0 30px;
            padding: 0 20px;
        }
        
        .features {
            display: flex;
            justify-content: space-around;
            margin: 30px 0;
            flex-wrap: wrap;
        }
        
        .feature {
            flex-basis: 30%;
            margin: 10px;
            padding: 20px;
            background: #f8f9fa;
            border-radius: 5px;
        }
        
        .feature h3 {
            color: #3498db;
            margin-bottom: 10px;
        }
        
        .buttons {
            margin: 30px 0;
        }
        
        .btn {
            display: inline-block;
            padding: 12px 30px;
            margin: 10px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 1em;
            transition: all 0.3s ease;
            text-decoration: none;
            color: white;
        }
        
        .btn-primary {
            background-color: #3498db;
        }
        
        .btn-secondary {
            background-color: #2ecc71;
        }
        
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            opacity: 0.9;
        }
        
        @media (max-width: 768px) {
            .container {
                margin: 20px;
                padding: 20px;
            }
            
            .feature {
                flex-basis: 100%;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>StudyTask</h1>
        
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <div class="description">
                    <p>Welcome back, ${sessionScope.user.login}!</p>
                </div>
                <div class="buttons">
                    <a href="tasks" class="btn btn-primary">My Tasks</a>
                    <a href="groups" class="btn btn-primary">My Groups</a>
                    <a href="profile" class="btn btn-primary">Profile</a>
                    <form action="logout" method="post" style="display: inline;">
                        <button type="submit" class="btn btn-secondary">Logout</button>
                    </form>
                </div>
            </c:when>
            <c:otherwise>
                <div class="description">
                    <p>A comprehensive task management system for students</p>
                </div>
                
                <div class="features">
                    <div class="feature">
                        <h3>Task Management</h3>
                        <p>Organize and track your assignments</p>
                    </div>
                    <div class="feature">
                        <h3>Group Projects</h3>
                        <p>Collaborate with your classmates</p>
                    </div>
                    <div class="feature">
                        <h3>Progress Tracking</h3>
                        <p>Monitor your academic progress</p>
                    </div>
                </div>
                
                <div class="buttons">
                    <a href="login.jsp" class="btn btn-primary">Login</a>
                    <a href="register.jsp" class="btn btn-secondary">Register</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>