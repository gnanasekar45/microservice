package com.luminn.firebase.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="quiz")
@Getter
@Setter
public class Quiz {

    @Id
    public String id;
    public String name;
    public String question1;
    public String question2;
    public String question3;
    public String question4;

    public String answers1;
    public String answers2;
    public String answers3;
    public String answers4;
    public int count;
    public String type;
    public String category;

}
