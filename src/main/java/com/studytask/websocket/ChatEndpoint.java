package com.studytask.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.studytask.dao.ChatDAO;
import com.studytask.exceptions.DAOException;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;


@ServerEndpoint("/chat/{userId}/{targetId}")
public class ChatEndpoint {
	private static final Map<Integer, Set<Session>> userSessions = new ConcurrentHashMap<>();


	@OnOpen
	public void onOpen(Session session, @PathParam("userId") int userId) {
	    userSessions.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet()).add(session);
	    System.out.println("User " + userId + " connected. Total sessions: " + userSessions.get(userId).size());
	}

    
	@OnClose
	public void onClose(Session session) {
	    userSessions.forEach((userId, sessions) -> {
	        sessions.removeIf(sess -> sess.getId().equals(session.getId()));
	    });
	    System.out.println("Disconnected from session: " + session.getId());
	}


    
	@OnMessage
	public void onMessage(String message, Session session, 
	                      @PathParam("userId") int senderId, 
	                      @PathParam("targetId") int targetId) throws DAOException {
	    
	    System.out.println("Message received: " + senderId + " -> " + targetId + ": " + message);
	    
	    // Save message
	    ChatDAO.sendMessage(message, senderId, targetId);

	    // Broadcast to all target user sessions
	    Set<Session> targetSessions = userSessions.get(targetId);
	    if (targetSessions != null) {
	        for (Session targetSession : targetSessions) {
	            if (targetSession.isOpen()) {
	                try {
	                    targetSession.getBasicRemote().sendText(message);
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    }
	}

}
