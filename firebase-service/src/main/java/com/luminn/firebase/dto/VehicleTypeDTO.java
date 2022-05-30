package com.luminn.firebase.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ch on 2/10/2016.
 */

public class VehicleTypeDTO {
    private String id;
    private String code;
    private String name;
    private String description;
    private String lang;

    @JsonCreator
    public VehicleTypeDTO(@JsonProperty("id") String id, @JsonProperty("code") String code, @JsonProperty("name") String name){
        this.id = id;
        this.code = code;
        this.description = description;
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
