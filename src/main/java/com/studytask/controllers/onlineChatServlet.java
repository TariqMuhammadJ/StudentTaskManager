package com.studytask.controllers;


import java.io.IOException;
import java.util.List;

import com.studytask.dao.ChatDAO;
import com.studytask.exceptions.DAOException;
import com.studytask.models.Chat;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class onlineChatServlet extends HttpServlet {
	
	private final ChatDAO chatDAO;
	
	public onlineChatServlet(ChatDAO chatDAO) {
		this.chatDAO = chatDAO;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			int senderId = Integer.parseInt(req.getParameter("userId"));
			int receiverId = Integer.parseInt(req.getParameter("targetId"));
			List<Chat> messages = chatDAO.getMessagesBetweenUsers(senderId, receiverId);
			req.setAttribute("chatMessages", messages);
			req.getRequestDispatcher("/chat.jsp").forward(req, resp);
		} catch (NumberFormatException | DAOException e) {
			e.printStackTrace();
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not load");
		}
	}

}
