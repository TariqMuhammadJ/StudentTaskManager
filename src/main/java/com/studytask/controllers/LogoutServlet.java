package com.studytask.controllers;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServlet;

@SuppressWarnings("serial")
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
	
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
			session.invalidate();
		}
		Res.sendRedirect("login.jsp");
	}
    
}
