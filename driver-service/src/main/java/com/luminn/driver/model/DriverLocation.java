package com.luminn.driver.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="driversLocation")
@Getter
@Setter
public class DriverLocation {
    private String name;
    private double latitude;
    private double longitude;
    private int status;
}
