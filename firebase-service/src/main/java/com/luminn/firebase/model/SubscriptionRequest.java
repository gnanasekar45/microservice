package com.luminn.firebase.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SubscriptionRequest {
	private String topic;
	List<String> tokens;
}
