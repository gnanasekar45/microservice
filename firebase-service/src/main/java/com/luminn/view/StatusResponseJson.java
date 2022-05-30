package com.luminn.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.json.JSONObject;
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusResponseJson extends StatusResponse {

    public JSONObject dataJson;

    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JSONObject getDataJson() {
        return dataJson;
    }

    public void setDataJson(JSONObject dataJson) {
        this.dataJson = dataJson;
    }
}
