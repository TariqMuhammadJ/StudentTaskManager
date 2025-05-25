package com.studytask.controllers;

import java.io.IOException;
import java.util.Optional;

import com.studytask.exceptions.DAOException;
import com.studytask.models.User;
import com.studytask.services.ServiceFactory;
import com.studytask.services.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServlet;

@SuppressWarnings("serial")
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
	private final UserService userservice = ServiceFactory.getUserService();
	
	@Override
	protected void doGet(HttpServletRequest Req, HttpServletResponse Res) throws ServletException, IOException{
		HttpSession session = Req.getSession(false);
		if (session != null) {
			Res.sendRedirect("tasks.jsp");
		}
		else {
			Res.sendRedirect("login.jsp");
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest Req, HttpServletResponse Res) throws ServletException, IOException{
	
		HttpSession session = Req.getSession(false);
		if (session != null) {
			Optional<User> user = (Optional<User>) session.getAttribute("user");
			System.out.println("User has successfully been logged out" + user.get().getId());
			session.invalidate();
		}
		Res.sendRedirect("login.jsp");
	}
    
}
