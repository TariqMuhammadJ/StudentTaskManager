<!DOCTYPE html>
<html>
<head>
    <title>Task Details</title>
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
        form {
            display: flex;
            flex-direction: column;
        }
        label, input, button {
            margin-bottom: 10px;
        }
        input {
            padding: 8px;
            font-size: 14px;
        }
        button {
            padding: 10px;
            background-color: #007BFF;
            color: white;
            border: none;
            cursor: pointer;
        }
        button:hover {
            background-color: #0056b3;
        }
        .nav-links {
            text-align: center;
            margin-top: 20px;
        }
        .nav-links a {
            color: #007BFF;
            text-decoration: none;
            margin: 0 10px;
        }
        .nav-links a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Task Details</h1>
        <form action="taskDetails" method="post">
            <input type="hidden" name="taskId" value="${task.id}">
            <label for="title">Title:</label>
            <input type="text" id="title" name="title" value="${task.title}" required>
            <br>
            <label for="description">Description:</label>
            <input type="text" id="description" name="description" value="${task.description}">
            <br>
            <label for="due_date">Due Date:</label>
            <input type="date" id="due_date" name="due_date" value="${task.dueDate}" required>
            <br>
            <label for="completed">Completed:</label>
            <input type="checkbox" id="completed" name="completed" ${task.completed ? "checked" : ""}>
            <br>
            <button type="submit">Update Task</button>
        </form>
        <div class="links">
            <a href="tasks.jsp">Back to Tasks</a>
        </div>
    </div>
</body>
</