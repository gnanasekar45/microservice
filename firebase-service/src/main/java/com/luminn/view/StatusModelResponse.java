package com.luminn.view;

import com.luminn.firebase.model.ModelStatus;

public class StatusModelResponse {

    ModelStatus status;

    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ModelStatus getStatus() {
        return status;
    }

    public void setStatus(ModelStatus status) {
        this.status = status;
    }
}
