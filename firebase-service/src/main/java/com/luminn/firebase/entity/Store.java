package com.luminn.firebase.entity;


import com.luminn.firebase.util.SMSType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.io.Serializable;
import java.util.Date;

@Document(collection = "store")
@Getter
@Setter
public class Store implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;


    private String key;

    private String value;

    private String text;

    private Date createdDate;

    private SMSType type;


    private Date updatedOn;


    public void updatedOn() {
        updatedOn = new Date();
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public SMSType getType() {
        return type;
    }

    public void setType(SMSType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}