package com.luminn.firebase.model;

public class ResponseStatus {

    ModelStatus status;
    String id;

    public ModelStatus getStatus() {
        return status;
    }

    public void setStatus(ModelStatus status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
