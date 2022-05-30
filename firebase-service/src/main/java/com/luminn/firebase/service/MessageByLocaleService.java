package com.luminn.firebase.service;


public interface MessageByLocaleService {

	String getMessage(String id);
	String getMessage(String id, Object... params);
}
