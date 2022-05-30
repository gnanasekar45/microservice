package com.luminn.firebase.config;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseInitializer {
	@Value("${firebase.url}")
	private String projectUrl;

	@PostConstruct
	public void init() throws IOException {
		InputStream serviceAccount = new ClassPathResource("accountKey.json").getInputStream();

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setDatabaseUrl(projectUrl).build();

		FirebaseApp.initializeApp(options);
	}
}
