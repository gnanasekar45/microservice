package com.luminn.firebase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

@Configuration
public class FirebaseConfig {
	@Bean
	public Firestore getDatabase() {
		return FirestoreClient.getFirestore();
	}
}
