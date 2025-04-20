<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, java.time.*" %>
<%
    String userId = request.getParameter("targetid");
    // Simulate fetching a username (replace with real DB logic if needed)
    String username = "User " + userId;

    // You can later pull messages from a database or session
    List<String> messages = Arrays.asList(
        "Hello!",
        "How are you?",
        "Let's catch up soon."
    );
%>
<!DOCTYPE html>
<html>
<head>
    <title>Chat with <%= username %></title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #eef2f5;
            margin: 0;
            padding: 0;
        }
        .chat-container {
            width: 500px;
            margin: 40px auto;
            background: #fff;
            padding: 20px;
            box-shadow: 0 0 8px rgba(0,0,0,0.1);
        }
        h2 {
            text-align: center;
            margin-bottom: 20px;
        }
        .messages {
            border: 1px solid #ccc;
            height: 250px;
            overflow-y: auto;
            padding: 10px;
            background: #fdfdfd;
            margin-bottom: 15px;
        }
        .message {
            margin: 10px 0;
            padding: 8px;
            background: #e0f7fa;
            border-radius: 5px;
            max-width: 70%;
        }
        .message.you {
            background: #dcedc8;
            margin-left: auto;
        }
        form input[type="text"] {
            width: 80%;
            padding: 10px;
            border: 1px solid #bbb;
            border-radius: 4px;
        }
        form button {
            padding: 10px 15px;
            background: #007BFF;
            border: none;
            color: #fff;
            border-radius: 4px;
            cursor: pointer;
        }
        form button:hover {
            background: #0056b3;
        }
        .back {
            display: block;
            margin-top: 20px;
            text-align: center;
        }
        .back a {
            text-decoration: none;
            color: #007BFF;
        }
        .back a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="chat-container">
    <h2>Chat with <%= username %></h2>

    <div class="messages">
        <% for(String msg : messages) { %>
            <div class="message"><%= msg %></div>
        <% } %>
        <div class="message you">This is your message</div>
    </div>

    <form id="message-form">
        <input type="hidden" name="userId" value="<%= userId %>" />
        <input type="text" id="message"  placeholder="Type your message..." required />
        <button type="submit" id="chat-send">Send</button>
    </form>

    <div class="back">
        <a href="onlineUsers" id="back">← Back to Online Users</a>
    </div>
</div>

<script type="module"  src="./JS/chatlive.js"></script>

</body>
</html>
