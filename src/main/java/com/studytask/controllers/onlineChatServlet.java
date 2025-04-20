package com.studytask.controllers;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.studytask.dao.ChatDAO;
import com.studytask.exceptions.DAOException;
import com.studytask.models.Chat;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/allChats")
public class onlineChatServlet extends HttpServlet {
	
	private ChatDAO chatDAO;
	public onlineChatServlet() {
		this.chatDAO = new ChatDAO();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			int senderId = Integer.parseInt(req.getParameter("userId"));
			int receiverId = Integer.parseInt(req.getParameter("targetId"));
			List<Chat> messages = chatDAO.getMessagesBetweenUsers(senderId, receiverId);
			for (Chat message : messages) {
				System.out.println(message);
			}
			Gson gson = new GsonBuilder()
					.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
					.create();
			String json = gson.toJson(messages);
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(json);
			//req.setAttribute("chatMessages", messages);
			//req.getRequestDispatcher("/chat.jsp").forward(req, resp);
		} catch (NumberFormatException | DAOException e) {
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write("{\"error\": \"Failed to fetch messages \"}");
			e.printStackTrace();
		}
	}

}
