package com.luminn.firebase.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


@Document(collection = "vechicleType")
@Getter
@Setter
public class VehicleType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;


    private String code;

    private String lang;
    private String name;


    public String getId() {
        return id;
    }

    private String description;



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setId(String id) {
        this.id = id;
    }
}
