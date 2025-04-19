package com.studytask.websocket;

import java.io.IOException;
import java.util.Map;
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
    private static final Map<Integer, Session> userSessions = new ConcurrentHashMap<>();
    private final ChatDAO chatDao;
    
    public ChatEndpoint(ChatDAO chatDao) {
    	this.chatDao = chatDao;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") int userId){
        userSessions.put(userId, session);
        System.out.println("user" + userId + "Connected");
        System.out.println("User session " + userSessions.get(userId));
    }
    
    @OnClose
    public void onClose(Session session) {
    	userSessions.values().removeIf(sess -> sess.getId().equals(session.getId()));
    	System.out.println("Disconnected from" + session.getId());
    	
    }

    
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("userId") int senderId, @PathParam("targetId") int targetId) throws DAOException {
    	System.out.println("Message received : " + senderId + ": " + message);
    	Session targetSession = userSessions.get(targetId);
    	
    	if (targetSession != null && targetSession.isOpen()) {
    		try {
    			targetSession.getBasicRemote().sendText(message);
    			chatDao.sendMessage(message, senderId, targetId);
    			
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    	else {
    		chatDao.sendMessage(message, senderId, targetId);
    	}
    	
    }
}
