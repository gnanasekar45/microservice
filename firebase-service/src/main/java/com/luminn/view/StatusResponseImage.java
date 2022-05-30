package com.luminn.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusResponseImage extends StatusResponse {

    private static final Logger log = LoggerFactory.getLogger(StatusResponseImage.class);

    private byte[] dataByte;

    public byte[] getDataByte() {
        return dataByte;
    }

    public void setDataByte(byte[] dataByte) {
        this.dataByte = dataByte;
    }
}
