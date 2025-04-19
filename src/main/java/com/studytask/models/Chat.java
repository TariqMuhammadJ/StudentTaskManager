package com.studytask.models;

import java.time.LocalDateTime;

public class Chat {
    private int chatId;
    private String chatText;
    private int senderId;
    private int sentToId;
    private LocalDateTime sentAt;

    public Chat(int chatId, String chatText, int senderId, int sentToId, LocalDateTime sentAt) {
        this.chatId = chatId;
        this.chatText = chatText;
        this.senderId = senderId;
        this.sentToId = sentToId;
        this.sentAt = sentAt;
    }

    public int getChatId(){
        return this.chatId;
    }

    public String getChatText(){
        return this.chatText;
    }

    public void setChatText(String chatText){
        this.chatText = chatText;
    }

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public int getSentToId() {
		return sentToId;
	}

	public void setSentToId(int sentToId) {
		this.sentToId = sentToId;
	}

	public LocalDateTime getSentAt() {
		return sentAt;
	}

	public void setSentAt(LocalDateTime sentAt) {
		this.sentAt = sentAt;
	}
    
    // Getters and Setters
}
