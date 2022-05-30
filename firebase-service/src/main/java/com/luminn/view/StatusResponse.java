package com.luminn.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusResponse {

    private static final Logger log = LoggerFactory.getLogger(StatusResponse.class);

    private int statusCode = 0;
    private boolean status = false;
    private String message;
    private Object data;
    private String jwt;
    private int infoId=850;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }



    public Object getData()
    {
        return this.data;
    }

    public void setData(Object jsonObject)
    {
        this.data = jsonObject;
    }

    public StatusResponse()
    {
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof StatusResponse))
        {
            return false;
        }

        StatusResponse response = (StatusResponse) o;

        if (statusCode != response.statusCode)
        {
            return false;
        }
        if (data != null ? !data.equals(response.data) : response.data != null)
        {
            return false;
        }
        if (message != null ? !message.equals(response.message) : response.message != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = statusCode;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "StatusResponse{" +
                "statusCode=" + statusCode +
                ", errorMessage='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public int getInfoId() {
        return infoId;
    }

    public void setInfoId(int infoId) {
        this.infoId = infoId;
    }


}
