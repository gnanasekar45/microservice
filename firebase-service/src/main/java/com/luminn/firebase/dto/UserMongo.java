package com.luminn.firebase.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="userMongo")
@Getter
@Setter
public class UserMongo {
    @Indexed(unique = true)
    private long Id;
    private String token;
    private double latitude;
    private double longitude;
    private String firstName;
    private String lastName;


}
