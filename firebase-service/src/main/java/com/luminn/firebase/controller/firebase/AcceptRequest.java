package com.luminn.firebase.controller.firebase;

public class AcceptRequest {

	private String token, messageId;

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMessageId() {
		return messageId;
	}

	public String getToken() {
		return token;
	}

	@Override
	public String toString() {
		return "AcceptRequest [token=" + token + ", messageId=" + messageId + "]";
	}
	
	
	

}
