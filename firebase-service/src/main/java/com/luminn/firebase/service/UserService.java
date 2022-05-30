package com.luminn.firebase.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.luminn.firebase.exception.UserExistException;
import com.luminn.firebase.request.User;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class UserService {
	
	@Autowired
	private Firestore firestore;
	
	@Value("${firebase.collection}")
	private String collectionName;
	
	private CollectionReference collection() {
		return this.firestore.collection(collectionName);
	}
	
	public User getUser(String email) throws InterruptedException, ExecutionException {
		Query userQuery = this.collection().whereEqualTo("email", email);
		QuerySnapshot futureDoc = userQuery.get().get(); 
		if (!futureDoc.getDocuments().isEmpty()) {
			QueryDocumentSnapshot userInfo = futureDoc.getDocuments().get(0);
			if (userInfo.exists()) {
				//log.info("user found!");
				return userInfo.toObject(User.class);
			}
		}
		//log.info("user doesn't exist!");
		return null;
	}
	public List<User> getAllUsers() throws InterruptedException, ExecutionException {
		QuerySnapshot futureDoc = this.collection().get().get();
		List<User> users = new ArrayList<>();
		futureDoc.getDocuments().forEach(doc -> {
			users.add(doc.toObject(User.class));
		});
		return users;
	}
	public void insertUser(User user) throws InterruptedException, ExecutionException, UserExistException {
		/*Query userQuery = this.collection().whereEqualTo("email", user.getEmail());
		QuerySnapshot futureDoc = userQuery.get().get(); 
		if (!futureDoc.getDocuments().isEmpty()) {
			QueryDocumentSnapshot userInfo = futureDoc.getDocuments().get(0);
			if (userInfo.exists()) {
				throw new UserExistException("this user already registered!");
			}
		}*/
		ApiFuture<WriteResult> result = this.collection().document().set(user);
		//log.info(result.get().getUpdateTime());
	}
}
