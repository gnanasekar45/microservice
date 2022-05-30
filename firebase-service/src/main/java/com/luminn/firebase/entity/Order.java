package com.luminn.firebase.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="order")
@Getter
@Setter
public class Order {

    @Id
    private String id;
    private String name;
    //private float latitude;
    //private float longitude;
    private String item;
    private String price;
    private String kg;
    private float totalPrice;
    private float tax;
    private String comment;
    private String userId;
}
